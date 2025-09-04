package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class DoOnDisposeExample {
    public static void main(String[] args) throws InterruptedException {
        // Observable 을 소비자가 구독을 해지하는 시점에 로직 수행한다.
        // 완료 또는 에러로 종료되면 실행되지 않는다.

        // !! DoOnDispose 도 있지만 DoOnCancel 도 있다.
        //  DoOnCancel은 Flowable 에서 소비자가 구독을 해지하는 시점에 실행된다.

        Observable.just(1, 2, 3, 4,5,6,7)
                .zipWith(Observable.interval(300L, TimeUnit.MILLISECONDS), (i, num) -> i + 0)
                .doOnDispose(() -> Logger.log(LogType.DO_ON_DISPOSE, "# 생산자: 구독 해제!!"))
                .subscribe(new Observer<Integer>() {
                    private long startTime;
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 1. 구독 시점에 disposable 을 주입받는다!
                        disposable = d;
                        startTime = System.nanoTime();
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        // 2. ON NEXT 시점에 통지된 데이터가 조건에 걸리면
                        Logger.log(LogType.ON_NEXT, "DATA 통지 : " +integer);
                        if (TimeUtil.getCurrentTime() - startTime < 1000) {
                            // 3. 구독을 취소한다.
                           Logger.log(LogType.PRINT, "# 소비자 : 구독 해지, 1000L 초과");
                           disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Thread.sleep(4000);
    }
}
