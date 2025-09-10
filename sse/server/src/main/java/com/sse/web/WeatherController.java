package com.sse.web;

import com.sse.domain.Weather;
import com.sse.repository.WeatherRepository;
import com.sse.sensor.HumiditySensor;
import com.sse.sensor.TemperatureSensor;
import com.sse.util.LogType;
import com.sse.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeatherController {
    final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private SseEmitter sseEmitter;
    private List<Disposable> disposables = new ArrayList<>();

    private final HumiditySensor humiditySensor;
    private final WeatherRepository weatherRepository;
    private final TemperatureSensor temperatureSensor;


    @CrossOrigin("*")
    @GetMapping("/stream/weather")
    public SseEmitter connectWeatherEvents() {
        sseEmitter = new SseEmitter(SSE_SESSION_TIMEOUT);

        /**
         * publish() 를 사용하게 되면, ConnectableObservable로 반환한다.
         * ConnectableObservable 는 Hot Publish 형태이다.
         *
         * 특징은 구독을 하더라도 connect() 메소드가 호출되기 전에는
         * 데이터가 절대 통지되지 않는다.
         *
         * .publish()는여러개의 구독자들에게 통지되는 데이터를 브로드 캐스팅 한다.
         * 두가지 처리를 동시에 하면 브로드캐스팅을 위해서 publish 를 실행하지만
         * 한가지 일만하면 굳이 publish 를 호출하지 않고 그냥 observable 로 구독해서 바로 해도 된다.
         */
        ConnectableObservable<Weather> observable =
                Observable.zip(
                    temperatureSensor.getTemperatureStream(),
                    humiditySensor.getHumidityStream(),
                        Weather::new
        ).publish();

        // 구독 해지할 수 있는 객체 (Disposable)
        Disposable disposableSend = sendWeatherData(observable);
        Disposable disposableSave = saveWeatherData(observable);
        disposables.addAll(Arrays.asList(disposableSend, disposableSave));

        observable.connect();

        this.dispose(sseEmitter, () -> {
            disposables.stream()
                    .filter(disposable -> !disposable.isDisposed())
                    .forEach(disposable -> {
                        disposable.dispose();
                        Logger.log(LogType.DO_ON_DISPOSE, "DISPOSE");
                    });
        });

        return sseEmitter;
    }

    private Disposable sendWeatherData(ConnectableObservable<Weather> observable) {
        return observable.subscribe(
                weather -> {
                    sseEmitter.send(weather);
                    Logger.log(LogType.ON_NEXT,
                            weather.getTemperature() + " , " + weather.getTemperature());
                },
                error -> Logger.log(LogType.ON_ERROR, error.getMessage())
        );
    }

    private Disposable saveWeatherData(ConnectableObservable<Weather> observable) {
        return observable.subscribe(
                weather -> {
                    weatherRepository.save(weather);
                    Logger.log(LogType.PRINT, "saved weather " + weather.toString());
                },
                error -> Logger.log(LogType.ON_ERROR, error.getMessage())
        );
    }

    private void dispose(SseEmitter sseEmitter, Runnable runnable) {
        // emmiter 개체가 sse 연결이 종료가 되거나, timeout 이벤트가 발생하면 해당 runnable 객체를 실행한다.
        sseEmitter.onCompletion(runnable);
        sseEmitter.onTimeout(runnable);
    }
}
