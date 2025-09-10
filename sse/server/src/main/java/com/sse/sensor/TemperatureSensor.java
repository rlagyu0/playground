package com.sse.sensor;

import com.sse.util.NumberUtil;
import com.sse.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensor {
    public Observable<Integer> getTemperatureStream() {
        return Observable.interval(0L, TimeUnit.SECONDS)
                .delay(item -> {
                    TimeUtil.sleep(NumberUtil.randomRange(1000,3000));
                    return Observable.just(item);
                })
                .map(notUse -> this.getTemperature());
    }

    private int getTemperature() {
        return NumberUtil.randomRange(-10,30);
    }
}
