package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DoOnNextExample {
    public static void main(String[] args) {
        Observable.just(1,2,3,4,5,6,7)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "#원본 데이터 통지   : " + data))
                .filter(data -> data < 4)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "#filter 적용 후   : " + data))
                .map(data -> "###  변형 ( " + data + " ) ###")
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, "# map 적용 후   : "  + data))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, "# 최종 데이터 : " + data));
    }
}
