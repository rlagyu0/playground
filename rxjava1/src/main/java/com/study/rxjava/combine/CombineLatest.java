package com.study.rxjava.combine;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class CombineLatest {
    public static void main(String[] args) throws InterruptedException {
        // obs1, obs2 의 각각 observable 의 가장 최근에 통지된 데이터를 합쳐서 반환
        //      0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20
        //  +        0,       1,       2,        3, ...
        //          2-0 , 3-0 , 4-0 , 5-0 , 5-1 , 6-1 , 7-1 , 7-2 , 8-2 , 9-2 , 10-2 , 11-2 , 11-3 , 12-3 , 13-3 , 14-3 , 14-4 , 15-4 , 16-4 , 16-5 , 17-5 , 18-5 , 19-5 , 19-6 , 19-7 , 19-8 , 19-9
        // 합칠 대상이 없으면 출력하지 않다가 두 Observable 에서 둘다 하나라도 발행해야 출력
        Observable<Long> obs1 = Observable.interval(100L, TimeUnit.MILLISECONDS).take(20);
        Observable<Long> obs2 = Observable.interval(300L, TimeUnit.MILLISECONDS).take(10);

        Observable.combineLatest(
                obs1,
                obs2,
                (a, b) -> a + "-" + b
        ).subscribe(data -> System.out.print(data + " , "));

        Thread.sleep(6000);
    }
}
