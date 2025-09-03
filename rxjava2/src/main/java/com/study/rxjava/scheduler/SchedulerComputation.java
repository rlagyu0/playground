package com.study.rxjava.scheduler;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;

public class SchedulerComputation {
    public static void main(String[] args) {
//        **논리적인 연산 처리** 시, 사용하는 스케쥴러 CPU 코어의 물리적 쓰레드 수를
//                              넘지 않는 범위에서 쓰레드를 생성한다.
//        **논리적인 연산 처리** 는 대기시간 없이 빠른 연산처리를 위하기 때문에 **해당 스레드를 사용한다.**
//        - ⚠️ 물리적 스레드를 넘어가게 되면 오히려 스레드 전환 비용이 더 발생할 수 있음.
        File[] files = new File("C:\\Program Files").listFiles();

        Observable.fromArray(files)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data.getName()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter(data -> data.isDirectory())
                .map(dir -> dir.getName())
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        TimeUtil.sleep(1000L);
    }
}
