package com.study.rxjava.combine;

import io.reactivex.rxjava3.core.Observable;

public class Zip {
    public static void main(String[] args) {
        // 데이터가 적은 것이 완료되면 해당 ZIP 은 끝난다.
        /**
         * 1A
         * 2B
         * 3C
         */
        Observable<Integer> obs1 = Observable.just(1, 2, 3);
        Observable<String> obs2 = Observable.just("A", "B", "C", "D", "E");

        Observable.zip(obs1, obs2, (n, s) -> n + s)
                .subscribe(System.out::println);
    }
}
