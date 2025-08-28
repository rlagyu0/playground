package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class DelaySubscription {
    public static void main(String[] args) throws InterruptedException {
        // 구독을 딜레이 시키는 기능 (5초 뒤에 생산자에게 구독을 전달한다.)
        // 구독 시점은 00초인데 실제 생산자에게 나 구독할게! 라고 전달하는건 5초 뒤에 전달한다
        // 그래서 DO ON NEXT 도 5초뒤에 실행되며, ON NEXT 도 5초뒤에 같이 실행된다.
        /**
         * print() | main | 00:00:23.261 | # 실행 시작 시간 사용법 1 : 00:00:23.260
         * onNext() | RxComputationThreadPool-1 | 00:00:28.337 | 1
         * onNext() | RxComputationThreadPool-1 | 00:00:28.338 | 2
         * onNext() | RxComputationThreadPool-1 | 00:00:28.338 | 3
         * onNext() | RxComputationThreadPool-1 | 00:00:28.338 | 4
         * onNext() | RxComputationThreadPool-1 | 00:00:28.338 | 5
         */
        Logger.log(LogType.PRINT, "# 실행 시작 시간 사용법 1 : " + TimeUtil.getCurrentTimeFormatted());
        Observable.just(1,2,3,4,5)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .delaySubscription(5000, TimeUnit.MILLISECONDS)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(10000);
    }
}
