package com.study.rxjava.combine;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Concat {
    public static void main(String[] args) throws InterruptedException {
        // =========================================== 이전 내용
        // Merge 는 observable 을 여러개 파라미터를 주어서 각각의 observable 에서 통지하는 순서에 따라
        // observable 이 만들어졌다.
        // 1번 observable 1,2,3
        // 2번 observable 4,5,6
        // 통지 시점이 1(1번), 2(2번), 3(1번), 4(2번), 5(1번), 6(2번) 이라고 한다면
        // 통지 시점 순서로 1,2,3,4,5,6 의 observable 이만들어지게됨
        // ===========================================
        // 하지만 Observable.concat 은 Observable 파라미터가 등록된 순서대로
        // -> 통지 시점과 상관 없이 Observable 을 기준으로
        // 1번 observable 1,2,3
        // 2번 observable 4,5,6
        // 1번 (1,2,3) 2번 (4,5,6)
        // 순서로 1,2,3,4,5,6 의 observable 이만들어지게됨
        // 만약 2번 observable 이 선순위이면
        // 2번 (4,5,6) 1번 (1,2,3)
        // 4,5,6,1,2,3 <- 순서로 됨.

        // 1, 2, 3, 4, 5
        Observable<Long> a = Observable.interval(500L, TimeUnit.MILLISECONDS).take(5);
        // 1001,1002,1003,1004,1005
        Observable<Long> b = Observable.interval(500L, TimeUnit.MILLISECONDS).take(5).map(n -> n + 1000);

        // a , b 순서
        /**
         * onNext() | RxComputationThreadPool-1 | 23:58:26.393 | 0
         * onNext() | RxComputationThreadPool-1 | 23:58:26.889 | 1
         * onNext() | RxComputationThreadPool-1 | 23:58:27.388 | 2
         * onNext() | RxComputationThreadPool-1 | 23:58:27.888 | 3
         * onNext() | RxComputationThreadPool-1 | 23:58:28.388 | 4
         * onNext() | RxComputationThreadPool-2 | 23:58:28.890 | 1000
         * onNext() | RxComputationThreadPool-2 | 23:58:29.390 | 1001
         * onNext() | RxComputationThreadPool-2 | 23:58:29.890 | 1002
         * onNext() | RxComputationThreadPool-2 | 23:58:30.390 | 1003
         * onNext() | RxComputationThreadPool-2 | 23:58:30.891 | 1004
         */
//        Observable.concat(a,b).subscribe(data -> Logger.log(LogType.ON_NEXT, data));
        // b , a 순서
        /**
         * onNext() | RxComputationThreadPool-1 | 23:58:55.505 | 1000
         * onNext() | RxComputationThreadPool-1 | 23:58:55.999 | 1001
         * onNext() | RxComputationThreadPool-1 | 23:58:56.499 | 1002
         * onNext() | RxComputationThreadPool-1 | 23:58:56.999 | 1003
         * onNext() | RxComputationThreadPool-1 | 23:58:57.499 | 1004
         * onNext() | RxComputationThreadPool-2 | 23:58:57.999 | 0
         * onNext() | RxComputationThreadPool-2 | 23:58:58.499 | 1
         * onNext() | RxComputationThreadPool-2 | 23:58:58.999 | 2
         * onNext() | RxComputationThreadPool-2 | 23:58:59.499 | 3
         * onNext() | RxComputationThreadPool-2 | 23:59:00.000 | 4
         */
        Observable.concat(b,a).subscribe(data -> Logger.log(LogType.ON_NEXT, data));
        Thread.sleep(6000);

    }
}
