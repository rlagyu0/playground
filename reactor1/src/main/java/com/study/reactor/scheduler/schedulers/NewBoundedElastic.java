package com.study.reactor.scheduler.schedulers;

import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class NewBoundedElastic {
    public static void main(String[] args) {
        // 1st param : 생성할 스레드의 갯수
        // 2nd param : 스레드도 사용중일 때는 대기해야하는 작업이 있다. 이때 각각의 스레드에 붙은 큐의 사이즈를 정함
        // 3rd param : thread 이름의 prefix

        // * 유저 스레드이기 때문에 사용후에 일정 시간동안 죽지 않는다.
        //    scheduler.dispose() 로 종료
        Scheduler scheduler = Schedulers.newBoundedElastic(2, 2, "I/O Thread");
//        Scheduler scheduler = Schedulers.newParallel("Parallel Thread", 4, true);

        // mono 를 생성해준다.
        Mono<Integer> mono = Mono.just(1).subscribeOn(scheduler);

        // task1 5초동안 수행
        mono.subscribe(data -> {
            Logger.log(" subscribe 1 start : ", data);
            TimeUtil.sleep(5000);
            Logger.log(" subscribe 1 end : ", data);
        });

        // task2 5초동안 수행
        mono.subscribe(data -> {
            Logger.log(" subscribe 2 start : ", data);
            TimeUtil.sleep(5000);
            Logger.log(" subscribe 2 end : ", data);
        });

        // 큐에 대기
        mono.subscribe(data -> {
            Logger.log(" subscribe 3 start : ", data);
        });

        // 큐에 대기
        mono.subscribe(data -> {
            Logger.log(" subscribe 4 start : ", data);
        });

        // 큐에 대기
        mono.subscribe(data -> {
            Logger.log(" subscribe 5 start : ", data);
        });

        // 큐에 대기
        mono.subscribe(data -> {
            Logger.log(" subscribe 6 start : ", data);
        });

    }
}

