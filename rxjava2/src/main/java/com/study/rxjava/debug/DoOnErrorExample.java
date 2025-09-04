package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DoOnErrorExample {
    public static void main(String[] args) {
        // 에러 발생 전에 실행
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .zipWith(Observable.just(1, 2, 3, 4, 5, 0, 3, 4, 2, 4), (a, b) -> a / b)
                .doOnError(
                        error -> Logger.log(LogType.DO_ON_COMPLETE, "#소비자 에러 발생 전! 끝!" + error.getMessage())
                ).subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error));
    }
}
