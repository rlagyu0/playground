package com.study.rxjava.subject;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class ReplaySubjectExample {
    public static void main(String[] args) {
        // 구독 시점에 이전에 이미 통지된 데이터가 있다면 이미 통지된 데이터 데이터를 지정한 갯수만큼 전달 받음.
        //        ReplaySubject<Object> objectReplaySubject = ReplaySubject.create(2);
        // 갯수를 지정하지 않는다면 구독 이전에 통지되었던 모든 데이터를 전달받는다.
        ReplaySubject<Object> objectReplaySubject = ReplaySubject.create();

        objectReplaySubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "소비자 1  => data : " + price));
        objectReplaySubject.onNext(1000);
        objectReplaySubject.onNext(2000);
        objectReplaySubject.onNext(3000);

        objectReplaySubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "소비자 2  => data : " + price));
        objectReplaySubject.onNext(4000);

        objectReplaySubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "소비자 3  => data : " + price));

        objectReplaySubject.onComplete();

        // 이미 통지가 끝난 이후에도 구독하면 지정한 갯수 또는 지정하지 않으면 모든 통지 데이터를 전달받음.
        objectReplaySubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "통지가 끝난 onComplete 후 소비자 4  => data : " + price));
    }
}
