package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TimeInterval {
    public static void main(String[] args) throws InterruptedException {
        // TimeInterval 함수는 데이터를 통지하는데에 걸린 시간 뿐 아니라
        // 데이터도 같이 통지한다.
        Observable.just(1,2,3,4,5)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "전위 DO_ON_NEXT " + data))
                .delay(item -> {
                    int time = 2000 + (int)(Math.random() * (5000 - 2000 + 1)) * item;
                    System.out.println("time = " + time);
                    return Observable.timer(time, TimeUnit.MILLISECONDS);
                })
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "후위 DO_ON_NEXT "  + data))
                .timeInterval()
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "마지막 DO_ON_NEXT  "  + data ))
                .subscribe(timed -> {
                    Logger.log(LogType.ON_NEXT, "걸린 시간 : " + timed.time()  + timed.unit() + " 통지받은 데이터 : " + timed.value());
                });

        Thread.sleep(60000);

    }
}
