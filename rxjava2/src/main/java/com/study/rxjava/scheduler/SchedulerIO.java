package com.study.rxjava.scheduler;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;

public class SchedulerIO {
    /**
     * I/O 처리 작업을 할 때 사용하는 스케쥴러
     * 네트워크 요청 처리, 각종 입/출력 작업, 데이터베이스 쿼리 등에 사용
     * 쓰레드 풀에서 쓰레드를 가져오거나 가져올 쓰레드가 없으면 새로운 쓰레드를 생성한다. (쓰레드의 재사용)
     * @param args
     */
    public static void main(String[] args) {
        File[] files = new File("C:\\Program Files").listFiles();

        Observable.fromArray(files)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data.getName()))
                .subscribeOn(Schedulers.io())
                .filter(data -> data.isDirectory())
                .map(dir -> dir.getName())
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        TimeUtil.sleep(1000L);

    }
}
