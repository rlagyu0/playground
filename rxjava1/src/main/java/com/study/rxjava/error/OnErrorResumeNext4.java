package com.study.rxjava.error;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class OnErrorResumeNext4 {
    public static void main(String[] args) throws InterruptedException {
        // 에러가 발생하면 onErrorReturn 의 경우 하나의 데이터만 반환했는데
        // OnErrorResumeNext 는 에러를 의미하는 Observable 을 반환한다.
        // 에러처리를 위한 추가작업이 가능하고, 데이터 교체가 가능하다.

        // 0.5 초 간격으로 0,1,2,3,4 를 출력하는 옵저버블을 5를 나눠서 반환하려고 했으나
        // 5/0 에서 에러가 발생하여 0.2 초 간격으로 1,2,3,4,5,6,7,8,9 를 출력하고 5를 1-9까지 나누는 옵저버블로
        // 갈아끼워서 다시 리턴한다.
        Observable.just(5)
                .flatMap(num -> Observable.interval(500L, TimeUnit.MILLISECONDS)
                        .take(5)
                        .map(i -> num / i)
                        .onErrorResumeNext(throwable -> {
                            Logger.log(LogType.ON_ERROR, "운영자에게 이메일도 발송 가능" + throwable.getMessage());
                            return Observable.interval(200L, TimeUnit.MILLISECONDS)
                                    .take(10)
                                    .skip(1)
                                    .map(i -> num / i);
                        })
                ).subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(10000L);
    }
}
