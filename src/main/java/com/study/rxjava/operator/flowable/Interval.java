package com.study.rxjava.operator.flowable;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Interval {

    public static void main(String[] args) {
        // 일정 기간 대기 한 후 시간이 지나면 반복적으로 통지하는 것.
        // 완료 없이 계속 통지한다.
        // 최초 대기 시간을 설정 가능한다.
        // 호출한 스레드와 별도의 스레드에서 실행한다.
        // polling 용도의 작업을 할 때 쉽게 사용 가능한다.
        Observable.interval(0, 1000L, TimeUnit.MILLISECONDS)
                .map(num -> num + "count")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        TimeUtil.sleep(3000);

    }
}
