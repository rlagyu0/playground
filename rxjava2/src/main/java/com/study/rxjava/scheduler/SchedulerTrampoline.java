package com.study.rxjava.scheduler;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulerTrampoline {
    public static void main(String[] args) {
//        현재 실행되고 있는 (스레드를 생성하지않고 메인스레드 사용)
//        쓰레드에 큐를 생성하여 처리할 작업을 FIFO 방식으로 처리한다.
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5);

        just.subscribeOn(Schedulers.trampoline())
                .map(data -> "##" + data + "##")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        just.subscribeOn(Schedulers.trampoline())
                .map(data -> "@@" + data + "@@")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
