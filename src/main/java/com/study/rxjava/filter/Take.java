package com.study.rxjava.filter;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Take {
    public static void main(String[] args) throws InterruptedException {
        // 1. take (2) 를 하면 2번째 까지만 A,B 만 대상
        //     take (3) 를 하면 3번째 까지만 A,B,C 만 대상
        Observable.just(
                "A",
                "B",
                "C",
                "D",
                "E").take(2).subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        // 2. 시간으로도 가능하다. Interval 로 1초 간격으로 발행을 하지만, 3.5 초가 데드라인이라 0,1,2 (3초) 까지만 발행한다.
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .take(3500L, TimeUnit.MILLISECONDS)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        Thread.sleep(6000);
    }
}
