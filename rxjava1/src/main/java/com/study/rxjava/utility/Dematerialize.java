package com.study.rxjava.utility;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Dematerialize {
    public static void main(String[] args) throws InterruptedException {
        // dematerialize
        // 통지된 Notification 객체를 원래의 통지 데이터로 변환해서 통지한다.
        Observable.concatEager(Observable.just(getAObservable().subscribeOn(Schedulers.io()),
                                                                                getBObservable().subscribeOn(Schedulers.io())
                                                                                        .materialize()
                                                                                        .map(stringNotification -> {
                                                                                            if(stringNotification.isOnError()) {
                                                                                                Logger.log(LogType.ON_ERROR, "에러 발생!!!!!!");
                                                                                            }
                                                                                            return stringNotification;
                                                                                        })
                                                                                        .filter(stringNotification -> !stringNotification.isOnError())
                                                                                        .dematerialize(stringNotification ->  stringNotification)

        ))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, "data : " + data),
                        error -> Logger.log(LogType.ON_ERROR, error.getMessage())
                );


        Thread.sleep(6000);

    }

    public static @NonNull Observable<String> getAObservable() {
        return Observable.just("item1", "item2", "item3", "item4", "item5");
    }

    public static @NonNull Observable<String> getBObservable() {
        return Observable.just("ob1", "ob2", "ob3", "ob4", "ob5")
                .map(ob -> {
                    if (ob.equals("ob3")) {
                        throw new RuntimeException("ob3");
                    }
                    return ob;
                });
    }
}
