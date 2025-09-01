package com.study.rxjava.condition;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class SequenceEqual {
    public static void main(String[] args) throws InterruptedException {
        // 여러개의 생산자가 존재할 때
        // 순서대로 각각의 인덱스의 값을 비교해서 같지 않은 것을 만나면 false 를 반환한다.
        // 통지 시간과는 다르더라도 순서대로 무조건 된다.
        // ex
        // 1번 Observable 1,2,3,4
        // 2번 Observable 1,2,4,6
        // 순서대로
        // 1st -> 1=1 동일 (결과 패스)
        // 1nd -> 2=2 동일 (결과 패스)
        // 1rd -> 3=4 다름 (false 반환)
        // 통지는 되어서 do on next 는 호출하지만 false/true 를 반환하고나면 통지는 되긴돼...
        /**
         * doOnNext() | main | 23:25:40.965 | 1
         * doOnNext() | main | 23:25:40.967 | 2
         * doOnNext() | main | 23:25:40.967 | 3
         * doOnNext() | main | 23:25:40.967 | 4
         * doOnNext() | main | 23:25:40.967 | 1
         * doOnNext() | main | 23:25:40.967 | 2
         * doOnNext() | main | 23:25:40.967 | 4
         * onNext() | main | 23:25:40.967 | false
         * doOnNext() | main | 23:25:40.967 | 6 => 통지는 되어서 do on next 는 호출하지만 false/true 를 반환하고나면 통지는 되긴돼...
         */

        // 1번 Observable
        Observable<Integer> just1 = Observable.just(1, 2, 3, 4).doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data));
        // 2번 Observable
        Observable<Integer> just2 = Observable.just(1, 2, 4, 6).doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data));

        // 인덱스 3 -> 3=4 다름 (false 반환)
        Observable.sequenceEqual(just1, just2)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(6000);


        // 같을 경우 (true) 를 반환하는 경우
        // 1번 Observable
        just1 = Observable.just(1, 2, 3, 4).doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data));
        // 2번 Observable
        just2 = Observable.just(1, 2, 3, 4).doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data));

        // 모든 데이터가 동일해서 true 반환
        Observable.sequenceEqual(just1, just2)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}

