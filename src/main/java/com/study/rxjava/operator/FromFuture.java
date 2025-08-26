package com.study.rxjava.operator;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.CompletableFuture;

public class FromFuture {
    // 자바5 에서 새로 생성된 연산자
    // 비동기 처리를 위한 API
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Double> doubleCompletableFuture = longTimeWork();

        shortWork();

        Observable.fromFuture(doubleCompletableFuture)
                .subscribe(data -> Logger.log(LogType.PRINT, "#긴 처리 시간 작업 결과 : " + data));

    }

    public static CompletableFuture<Double> longTimeWork() {return CompletableFuture.supplyAsync(() -> {
        try {
            return calculate();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    });}

    private static Double calculate() throws InterruptedException {
        Logger.log(LogType.PRINT, "# 긴 시간의 작업이 시작되었습니다.");
        Thread.sleep(6000);
        Logger.log(LogType.PRINT, "# 긴 시간의 작업이 완료되었습니다.");
        return 30303030303.0;
    }

    private static void shortWork() throws InterruptedException {
        Logger.log(LogType.PRINT, "# 짧은 시간의 작업이 시작되었습니다.");
        Thread.sleep(3000);
        Logger.log(LogType.PRINT, "# 짧은 시간의 작업이 완료되었습니다.");
    }

}
