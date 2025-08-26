package com.study.rxjava.operator.flowable;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class Timer {
    public static void main(String[] args) {
        // 지정한 시간이 지나면 0을 통지한다.
        // 0을 통지하고 onComplete 이벤트가 발생 후 종료
        // 메인 스레드와는 별도의 스레드에서 실행된다.
        // 특정 시간을 대기한 후에 다음 처리를 하고자 할 때 사용한다.
        Logger.log(LogType.PRINT, "# start !");
        Observable<String> observable = Observable.timer(2000, TimeUnit.MILLISECONDS)
                .map(count -> "Do work!");
        observable.subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        TimeUtil.sleep(3000);
    }
}
