package com.study.rxjava.operator;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.time.LocalTime;

public class Defer {
    public static void main(String[] args) throws InterruptedException {
        // 구독이 발생할 때 마다 즉, subscribe 가 호출될 때마다 새로운 Observable 을 생성한다.
            // -> 즉 타임라인이 호출될 때마다 새로 생긴다.
        // 선언한 시점의 데이터를 통지하는 것이 아니라 호출 시점의 데이터를 통지한다.
            // -> just (선언한 시점)
            // print() | main | 22:56:34.651 | # just 구독 1의 time : 22:56:34.636568
            // print() | main | 22:56:39.654 | # just 구독 2의 time : 22:56:34.636568
            // -> defer (구독 시점)
            // print() | main | 22:56:34.649 | # defer 구독 1의 time : 22:56:34.644718300
            // print() | main | 22:56:39.652 | # defer 구독 2의 time : 22:56:39.652964900
        // 데이터를 생성을 미루는 효과가 있기 때문에 최신 데이터를 얻고자할 때 사용한다.
            // -> 구독 시점에 데이터를 생성하기 때문에 호출 시점에 최신 데이터를 얻을 수 있음.

        Observable<LocalTime> defer = Observable.defer(() -> {
            LocalTime currentTime = LocalTime.now();
            return Observable.just(currentTime);
        });

        Observable<LocalTime> just = Observable.just(LocalTime.now());
        defer.subscribe(time -> Logger.log(LogType.PRINT, "# defer 구독 1의 time : " + time));
        just.subscribe(time -> Logger.log(LogType.PRINT, "# just 구독 1의 time : " + time));

        Thread.sleep(5000);

        defer.subscribe(time -> Logger.log(LogType.PRINT, "# defer 구독 2의 time : " + time));
        just.subscribe(time -> Logger.log(LogType.PRINT, "# just 구독 2의 time : " + time));

    }
}
