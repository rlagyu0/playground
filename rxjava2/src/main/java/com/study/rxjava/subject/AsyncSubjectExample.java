package com.study.rxjava.subject;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.subjects.AsyncSubject;

public class AsyncSubjectExample {
    public static void main(String[] args) {
//         완료전까지 아무것도 통지를 하지 않음.
//         완료와 동시에 마지막으로 통지한 데이터와 완료 통지만 함.
//         구독 시점에 상관 없이 마지막 데이터 통지만 받는다.
//         * 완료 후에 구독하더라도 마지막 통지된 데이터를 받을 수 있다.
        AsyncSubject<Object> subject = AsyncSubject.create();

        subject.onNext(1000);

        subject
                .doOnNext(price -> Logger.log(LogType.DO_ON_NEXT, "#소비자 1 통지 : " + price))
                .subscribe(price -> Logger.log(LogType.ON_NEXT, "#소비자 1 구독 : " + price));

        subject.onNext(2000);

        subject
                .doOnNext(price -> Logger.log(LogType.DO_ON_NEXT, "#소비자 2 통지 : " + price))
                .subscribe(price -> Logger.log(LogType.ON_NEXT, "#소비자 2 구독 : " + price));

        subject.onNext(3000);

        subject
                .doOnNext(price -> Logger.log(LogType.DO_ON_NEXT, "#소비자 3 통지 : " + price))
                .subscribe(price -> Logger.log(LogType.ON_NEXT, "#소비자 3 구독 : " + price));

//        subject.onNext(4000);

        // 구독 시점에 상관 없이 마지막 데이터 통지만 받는다.
        // * 완료 후에 구독하더라도 마지막 통지된 데이터를 받을 수 있다.
        // 의 예)
        subject.onComplete();

        subject
                .doOnNext(price -> Logger.log(LogType.DO_ON_NEXT, "#소비자 4 통지 : " + price))
                .subscribe(price -> Logger.log(LogType.ON_NEXT, "#소비자 4 구독 : " + price));

    }
}
