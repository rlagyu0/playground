package com.study.rxjava.operator.flowable;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class Range {
    public static void main(String[] args) {
        // 지정값 n 부터 m 개의 숫자를 통지한다.
        // for 문이나 while 대신에 사용한다.
        Observable<Integer> range = Observable.range(0, 5);
        range.subscribe(num -> Logger.log(LogType.ON_NEXT, num));
    }
}
