package com.study.rxjava.condition;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Amb {
    public static void main(String[] args) throws InterruptedException {
        // 여러개의 Observable 에서 최초 통지 시점이 가장 빠른
        // Observable 의 데이터만 통지되고 나머지는 무시된다.
        // 즉, 가장 먼저 통지를 시작한 Observable 의 데이터만 통지된다.
        Observable<Integer> obs1 = Observable.just(1, 2, 3, 4)
                .delay(4000, TimeUnit.MILLISECONDS);

        Observable<Integer> obs2 = Observable.just(5,6,7,8)
                .delay(2000, TimeUnit.MILLISECONDS);

        Observable<Integer> obs3 = Observable.just(9,10)
                .delay(3000, TimeUnit.MILLISECONDS);

        // 1번은 4초, 2번은 2초, 3번은 3초 있다가 소비자에 소비된다.
        List<Observable<Integer>> list = Arrays.asList(obs1, obs2, obs3);

        // 제일 먼저 전송한 2번 Observable 이 통지된다.
        /**
         * onNext() | RxComputationThreadPool-2 | 22:49:50.168 | 5
         * onNext() | RxComputationThreadPool-2 | 22:49:50.171 | 6
         * onNext() | RxComputationThreadPool-2 | 22:49:50.171 | 7
         * onNext() | RxComputationThreadPool-2 | 22:49:50.171 | 8
         */
        // 나머지는 무시당한다.
        Observable.amb(list)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(5000);
    }
}
