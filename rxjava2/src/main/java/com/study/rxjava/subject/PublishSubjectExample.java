package com.study.rxjava.subject;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class PublishSubjectExample {
    public static void main(String[] args) {

        PublishSubject<Integer> publishSubject = PublishSubject.create();

        publishSubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "# 소비자 1 : " + price));
        publishSubject.onNext(3500);
        publishSubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "# 소비자 2 : " + price));
        publishSubject.onNext(3300);
        publishSubject.subscribe(price -> Logger.log(LogType.ON_NEXT, "# 소비자 3 : " + price));
        publishSubject.onNext(3400);

        publishSubject.subscribe(
            price -> Logger.log(LogType.ON_NEXT, "# 소비자 4 : " + price),
            error -> Logger.log(LogType.ON_ERROR, error.getMessage()),
                            () -> Logger.log(LogType.ON_COMPLETE)

        );

        publishSubject.onComplete();
    }
}
