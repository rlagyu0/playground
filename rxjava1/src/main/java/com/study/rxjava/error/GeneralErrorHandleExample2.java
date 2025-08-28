package com.study.rxjava.error;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class GeneralErrorHandleExample2 {
    public static void main(String[] args) throws InterruptedException {
        // 5 라는 타임라인에 + 1,2,3,4 라는 타임라인을 조합해서 결과를 낸다
        Observable.just(5)
                .flatMap(num ->
                        Observable.
                                interval(5000, TimeUnit.MILLISECONDS).
                                doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data)).
                        // doOnNext() | RxComputationThreadPool-1 | 22:18:28.229 | 0
                        // onERROR() | RxComputationThreadPool-1 | 22:18:28.231 | java.lang.ArithmeticException: / by zero
                                take(5).
                                map(data -> num / data)). // data 의 초기값은 0 이기 때문에 에러가 발생
                        subscribe(data -> Logger.log(LogType.ON_NEXT, data),
                                error -> Logger.log(LogType.ON_ERROR, error),
                                                    () -> Logger.log(LogType.ON_COMPLETE)
                        );

        Thread.sleep(6000);
    }
}
