package com.study.rxjava.transform;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class ConcatMap {

    public static void main(String[] args) throws InterruptedException {
        // flatMap 과 concatMap 의 같은점은 하나의 데이터를 여러개의 데이터 (Observable) 을 반환한다.
        // 차이점으로는 flatMap 은 순서를 보장하지 않는 반면에 concatMap 은 순서를 보장한다.
        // 단점으로는 concatMap 은 순서를 보장하여 선행된 데이터의 처리가 완료되지 않으면 다음이 실행이안된다.
        // 위 단점으로 인해 성능상 이슈가 존재할 수 있다.
        TimeUtil.start();

        // flatMap 으로 순서가 뒤죽박죽인 구구단 출력
        /**
         * .
         * .
         * .
         * onNext() | RxComputationThreadPool-11 | 00:55:43.122 | 10 * 7 = 70
         * onNext() | RxComputationThreadPool-11 | 00:55:43.122 | 8 * 9 = 72
         * onNext() | RxComputationThreadPool-11 | 00:55:43.122 | 9 * 8 = 72
         * onNext() | RxComputationThreadPool-10 | 00:55:43.322 | 9 * 9 = 81
         * onNext() | RxComputationThreadPool-10 | 00:55:43.322 | 10 * 8 = 80
         * onNext() | RxComputationThreadPool-11 | 00:55:43.522 | 10 * 9 = 90
         * # 실행시간: 4306 ms
         */
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .skip(1)
                .take(10)
                .flatMap(number -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + " = " + row * number))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data.toString()),
                                    error -> {},
                                    () -> {
                                        TimeUtil.end();
                                        TimeUtil.takeTime();
                                    }
                );
        Thread.sleep(6000);


        // concat map 은 순서를 보장하지만 성능이 느리다.
        // # 실행시간: 26522 ms
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .skip(1)
                .take(10)
                .concatMap(number -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + " = " + row * number))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data.toString()),
                        error -> {},
                        () -> {
                            TimeUtil.end();
                            TimeUtil.takeTime();
                        }
                );
        Thread.sleep(40000);
    }
}
