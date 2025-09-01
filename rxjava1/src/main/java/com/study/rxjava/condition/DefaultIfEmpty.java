package com.study.rxjava.condition;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DefaultIfEmpty {
    public static void main(String[] args) {
        // 생산자가 통지하는 데이터는 없다.
        // 만약, Observable 이 비어있으면 defaultIfEmpty 파라미터를 최종으로 통지한다.
        // 연산자 이름 의미 그대로 통지할 데이터가 없이 비어있는 상태일때 디폴트 값을 통지한다.

        // 즉, 필터가 10 이상인 값들만 통지하도록 하는데 1,3,4,6 은 10 이상이 아니라 통지할 데이터가 없다.
        // 그러므로.defaultIfEmpty(3) 3을 반환한다.
        Observable.just(1,3,4,6)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .filter(num -> num > 10)
                .defaultIfEmpty(3)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
