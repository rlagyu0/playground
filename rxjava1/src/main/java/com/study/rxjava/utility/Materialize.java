package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Materialize {
    public static void main(String[] args) throws InterruptedException {
        // materialize
        // 통지된 데이터와 통지된 데이터의 통지 타입 (onNext, onComplete)
        // 자체를 Notification 객체에 담고
        // Notification 객체를 통지한다. 즉, 통지 데이터의 메타 데이터를 포함해서 통지한다라고
        // 볼수있다.
        Observable.just(1, 2, 3, 4, 5, 6)
                .map(data -> {
                    long time = 1000;
                    if (4 == data) {
                        time = time * 2;
                    }
                    TimeUtil.sleep(time);
                    return data;
                })
                .timeout(1500, TimeUnit.MILLISECONDS)
                .materialize()
                .subscribe(notification -> {
                            String notificationType = "";
                            if (notification.isOnNext()) {
                                notificationType = "onNext";
                            }
                            if (notification.isOnError()) {
                                notificationType = "onError";
                            }
                            if (notification.isOnComplete()) {
                                notificationType = "onComplete";
                            }
                            Logger.log(LogType.PRINT, notificationType);
                            Logger.log(LogType.ON_NEXT, notification.getValue());
                        }
                );
        Thread.sleep(6000);

    }
}
