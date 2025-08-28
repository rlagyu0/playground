package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class TimeOut {
    public static void main(String[] args) throws InterruptedException {
        // 타임아웃에 셋팅한 시간이 2초라고 하면, 2초 이내에 데이터가 통지가 되지 않으면
        // 데이터는 통지되지 못하고 그다음 데이터도 통지하지 못하며 종료된다.
        // 즉, 지정된 시간안에 통지가 되지 않으면 에러를 통지한다.
        /**
         * 1 -> do on next
         * onNext() | main | 00:13:08.348 | datat : 1
         * 2 -> do on next
         * onNext() | main | 00:13:09.351 | datat : 2
         * 3 -> do on next
         * onNext() | main | 00:13:10.352 | datat : 3
         * !!!!! 통지가 늦었으니 일단 에러가 발생한다 !!!!!!!
         * onERROR() | RxComputationThreadPool-1 | 00:13:11.854 | The source did not signal an event for 1500 milliseconds and has been terminated.
         * 4 -> do on next 는 그대로 2초 뒤에 로그가 찍힘.
         */
        Observable.range(1,5)
                .map(num -> {
                    long time = 1000L;
                    // 나머지는 1초에 통지
                    if(num == 4) {
                        // 4만 2초 뒤에 통지
                        time = 2000L;
                    }
                    TimeUtil.sleep(time);
                    return num;
                })
                // 4는 일단 2초 뒤에 do on next 에 찍히지만 구독 부분에서 에러가 1.5초 경에 발생
                .doOnNext(System.out::println)
                // 1.5 초 이내에 통지되지 않는 데이터는 구독 부분에서 에러 발생한다.
                .timeout(1500, TimeUnit.MILLISECONDS)
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, "datat : " + data),
                        error -> Logger.log(LogType.ON_ERROR, error.getMessage())
                );

        Thread.sleep(6000);
    }
}
