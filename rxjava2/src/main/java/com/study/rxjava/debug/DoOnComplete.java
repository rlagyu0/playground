package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DoOnComplete {
    public static void main(String[] args) {
        // 구독 OnComplete 실행 전에 바로 실행
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE, "#소비자 구독 끝!"))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error),
                        () -> Logger.log(LogType.ON_COMPLETE));
    }
}
