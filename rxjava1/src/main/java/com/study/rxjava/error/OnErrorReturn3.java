package com.study.rxjava.error;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class OnErrorReturn3 {
    public static void main(String[] args) throws InterruptedException {
        // 에러가 발생했을 때 에러를 의미하는 데이터로 대체할 수 있다.
        // onErrorReturn() 을 호출하면 onError 이벤트는 발생하지 않는다.

        // 에러가 발생하면 -1 을 반환하면서 다음 구독을 하지 않고 중지한다.
        Observable.just(5)
                .flatMap(number ->
                        Observable.just(1,2,3,4,0,8,7,6).
                        take(8).
                        map(i -> number / i).
                        onErrorReturn(exception ->  {
                            if(exception instanceof ArithmeticException){
                                Logger.log(LogType.ON_NEXT, exception.getMessage() + " (return -1)");
                            }
                            return -1;
                        })
                ).subscribe(
                        data -> {
                            if (data < 0) {
                                Logger.log(LogType.PRINT, data.toString() + " 에러가 발생해서 subscribe 에서 출력하는 로깅");
                            } else {
                                Logger.log(LogType.ON_NEXT, data);
                            }
                        },
                        error -> {
                            Logger.log(LogType.ON_ERROR, error.getMessage() + " 에러 핸들러 이 로그는 찍히지 않음");
                        },
                        () -> Logger.log(LogType.ON_COMPLETE)
                );

        Thread.sleep(5000);
    }
}
