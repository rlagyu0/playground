package com.study.rxjava.debug;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class DoOnEachExample {
    public static void main(String[] args) {
        /**
         * doOnNext, doOnComplete, doOnError 를 한번에 처리할 수 있다.
         * - 즉, onNext, onComplete, onError 이 실행 전에 notification 에 정보를 담아서 확인 가능
         * Notification 객체를 파라미터로 받아서 처리한다.
         */
        Observable.just(1, 2, 3, 4)
                .doOnEach(notification -> {
                    if (notification.isOnNext()) {
                        Logger.log(LogType.ON_NEXT, "#### 데이터를 통지합니다 ! : " + notification.getValue());
                    }

                    if (notification.isOnError()) {
                        Logger.log(LogType.ON_ERROR, notification.getError());
                    }

                    if(notification.isOnComplete()) {
                        Logger.log(LogType.ON_COMPLETE, "통지가 끝났어요~");
                    }
                })
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error),
                        () -> Logger.log(LogType.ON_COMPLETE));
    }
}
