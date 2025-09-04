package com.study.rxjava.scheduler;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulerNewThread {
    public static void main(String[] args) throws InterruptedException {
//        요청시마다 매번 새로운 쓰레드를 생성한다.
//
//                매번 생성되면 쓰레드 비용도 많이 들고, 재사용도 되지 않는다.
        Observable<Integer> just = Observable.just(1,2,3,4,5,6,7,8,7,6);

        just.subscribeOn(Schedulers.newThread())
                .map(data -> "##" + data + "##")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        just.subscribeOn(Schedulers.newThread())
                .map(data -> "@@" + data + "@@")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        Thread.sleep(3000);
    }
}
