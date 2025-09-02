package com.study.rxjava.statistic;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class Count {
    public static void main(String[] args) throws InterruptedException {
        // Observable 이 통지한 데이터의 총 갯수를 통지한다.
        // 총 갯수만 통지하면 되므로 결과값은 Single 로 반환한다.
        // 데이터의 총 갯수를 통지하는 시점은 완료 통지를 받은 시점이다.
        Observable.just(1,3,4,6).count().subscribe(data -> Logger.log(LogType.ON_NEXT, data));
        Thread.sleep(3000);
    }
}
