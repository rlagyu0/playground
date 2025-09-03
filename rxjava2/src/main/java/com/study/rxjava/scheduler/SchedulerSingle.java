package com.study.rxjava.scheduler;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulerSingle {
    public static void main(String[] args) throws InterruptedException {
//        단일 쓰레드를 생성하여 처리 작업을 진행한다.
//        - 여러번 구독해도 공통으로 하나의 스레드만 사용한다.
        Observable<Integer> just = Observable.just(1,2,3,4,5,6,7,8,7,6);

        just.subscribeOn(Schedulers.single())
                .map(data -> "##" + data + "##")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        just.subscribeOn(Schedulers.single())
                .map(data -> "@@" + data + "@@")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(3000);
    }
}
