package com.study.rxjava.completable;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompletableCreate {
    public static void main(String[] args) {
        Completable completable = Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                // 데이터를 통지하는것이 아니라 특정 작업을 수행한 후, 완료를 통지한다.
                int sum = 0;
                for(int i =0; i < 100; i++){
                    sum += i;
                }
                Logger.log(LogType.PRINT, "# 합계: " + sum);

                emitter.onComplete();
            }
        });

        completable.subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        // 아무것도 하지 않음
                    }

                    @Override
                    public void onComplete() {
                        Logger.log(LogType.ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Logger.log(LogType.ON_ERROR, error);
                    }
                });

        TimeUtil.sleep(100L);


//        Completable completable = Completable.create(emitter -> {
//            // 데이터를 발행하는것이 아니라 특정 작업을 수행한 후, 완료를 통지한다.
//            int sum = 0;
//            for(int i =0; i < 100; i++){
//                sum += i;
//            }
//            Logger.log(LogType.PRINT, "# 합계: " + sum);
//
//            emitter.onComplete();
//        });
//
//        completable.subscribeOn(Schedulers.computation())
//                .subscribe(
//                        () -> Logger.log(LogType.ON_COMPLETE),
//                        error -> Logger.log(LogType.ON_ERROR, error)
//                );
//
//        TimeUtil.sleep(100L);
    }
}
