package com.study.rxjava.error;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Retry5 {
    public static void main(String[] args) throws InterruptedException {
        // 데이터 통지 중 에러가 발생했을 때 데이터를 통지를 다시 재시도한다.
        // onError 이벤트가 발생하면 subscribe 를 다시 호출하여 재구독한다.

        // 만약 1,2,3,4, 까지 구독하고 0에서 에러가 났으면 다시 처음부터 재구독한다.
        // 1,2,3,4 (에러) -> 재구독 -> 1,2,3,4 (에러) -> 재구독 (횟수만큼 반복)
        // 결국 재시도 횟수만큼 했는데도 에러가 발생하면 onErrorReturn 을 해서 -1 을 반환한다.
        // 0 다음은 구독하지 않는다.
        Observable.just(5)
                .flatMap(
                        num -> Observable
                                .just(1,2,3,4,0,9,8,7)
                                .map(i -> {
                                    long result;
                                    try {
                                        result = num / i;
                                    }catch (Exception e) {
                                        Logger.log(LogType.ON_ERROR, e.getMessage() + "에러발생!!");
                                        throw e;
                                    }
                                    return result;
                                })
                                .retry(5)
                                .onErrorReturn(throwable -> -1L)
                )
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data.toString() + "      -1 을 반환한거같아요"),
                        error -> Logger.log(LogType.ON_ERROR, error.getMessage()),
                        () -> Logger.log(LogType.ON_COMPLETE)
                );

        Thread.sleep(60000L);
    }
}
