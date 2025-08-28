package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Delay {
    public static void main(String[] args) throws InterruptedException {
        // 사용법 1 -> 단순히 원본데이터를 지연시키는 코드
        /**
         * print() | main | 23:41:33.788 | # 실행 시작 시간 : 23:41:33.787
         * doOnNext() | main | 23:41:33.866 | 1
         * doOnNext() | main | 23:41:33.868 | 3
         * doOnNext() | main | 23:41:33.868 | 4
         * doOnNext() | main | 23:41:33.868 | 6
         * print() | main | 23:41:33.868 | # 실행 완료 시간 : 23:41:33.868
         * onNext() | RxComputationThreadPool-1 | 23:41:37.869 | 1
         * onNext() | RxComputationThreadPool-1 | 23:41:37.869 | 3
         * onNext() | RxComputationThreadPool-1 | 23:41:37.869 | 4
         * onNext() | RxComputationThreadPool-1 | 23:41:37.869 | 6
         * onComplete() | RxComputationThreadPool-1 | 23:41:37.869
         */

        Logger.log(LogType.PRINT, "# 실행 시작 시간 사용법 1 : " + TimeUtil.getCurrentTimeFormatted());
        Observable.just(1,3,4,6)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .delay(4000, TimeUnit.MILLISECONDS)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data),
                        throwable -> Logger.log(LogType.ON_ERROR, throwable),
                        () -> Logger.log(LogType.ON_COMPLETE));

        Thread.sleep(10000);

        // 사용법 2
        // 여기서 return 으로 Observable 을 하는 이유
        // -- return Observable.just("옵저버블" + item, "옵저버블2" + item, "옵저버블3" + item);

        // Observable 의 내용에는 의미가 없다. 그냥 Observable 이 통지되는 시점에 데이터를 방출하라
        // 라는 신호 역할만 할뿐 데이터는 의미가 없다.
        Logger.log(LogType.PRINT, "# 실행 시작 시간 사용법 2 : " + TimeUtil.getCurrentTimeFormatted());
        Observable.just(1,3,5,7)
                .delay(item -> {
                    TimeUtil.sleep(1000L);
                    return Observable.just("옵저버블" + item, "옵저버블2" + item, "옵저버블3" + item);
                }).subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(10000);
    }
}
