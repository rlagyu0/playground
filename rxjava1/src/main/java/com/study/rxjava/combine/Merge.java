package com.study.rxjava.combine;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Merge {
    public static void main(String[] args) throws InterruptedException {
        // observable 이 두개가 존재하면 그 두개를 합쳐서 하나의 observable 로 반환
        // 1번 observable 1,2,3
        // 2번 observable 4,5,6
        // 통지 시점이 1(1번), 2(2번), 3(1번), 4(2번), 5(1번), 6(2번) 이라고 한다면
        // 통지 시점 순서로 1,2,3,4,5,6 의 observable 이만들어지게됨
        // 통지 시점이 제일 늦은 마지막 데이터가 통지되고 난 후에 하나의 observable 로 반환한다.
        Observable<Long> a = Observable.interval(200L, TimeUnit.MILLISECONDS).take(5);
        Observable<Long> b = Observable.interval(400L, TimeUnit.MILLISECONDS).take(5).map(num -> num + 1000);

        Observable.merge(a, b).subscribe(System.out::println);

        Thread.sleep(4000);
    }
}
