package com.study.rxjava.subject;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class BehaviorSubjectExample {
    public static void main(String[] args) {
        // 구독이 발생한 시점에 바로 이전 통지되었던 데이터를 통지 받고 그다음.
        // 데이터를 쭉 받는다.
        // subject 통지 1 ----------2----------3----------4
        // 첫(구독)시작 1 ----------2----
        //                          두번째 구독시작 2 ---3----------4
        // 즉, 이전에 마지막으로 통지됐던 데이터를 통지 받고 그다음 데이터를 통지받음.
        // 통지가 다 되고 구독하면 완료나 에러 통지만 전달 받음.
//        BehaviorSubject<Object> behaviorSubject = BehaviorSubject.createDefault(3000);
        BehaviorSubject<Object> behaviorSubject = BehaviorSubject.create();

        behaviorSubject.subscribe(price ->
                Logger.log(LogType.ON_NEXT, "# 소비자 1 : " + price));
        behaviorSubject.onNext(1000);

        behaviorSubject.subscribe(price ->
                Logger.log(LogType.ON_NEXT, "# 소비자 2: " + price));
        behaviorSubject.onNext(2000);

        behaviorSubject.subscribe(price ->
                Logger.log(LogType.ON_NEXT, "# 소비자 3: " + price));
        behaviorSubject.onNext(3000);
    }
}
