package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DoOnSubscribeExample {
    public static void main(String[] args) {
        // 구독 직전에 실행 되는 함수
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .doOnSubscribe(disposable -> Logger.log(LogType.DO_ON_NEXT, "#생산자 : 구독 처리 준비 완료"))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error),
                        () -> Logger.log(LogType.ON_COMPLETE));
    }
}
