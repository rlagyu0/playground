package com.study.rxjava.condition;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class Contains {
    public static void main(String[] args) {
        // 파라미터의 데이터가 Observable 에 포함되어있는지 판단
        // 결과값은 true/false Single 로 반환
        // 결과값 반환 시점은 순차적으로 하나씩 차례로 통지하다가 파라미터(3) 을 만나는 순간 결과 반환
        /**
         * doOnNext() | main | 23:11:13.831 | 1
         * doOnNext() | main | 23:11:13.834 | 3
         * onNext() | main | 23:11:13.834 | true
         */
        Observable.just(1,3,4,6)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .contains(3)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
