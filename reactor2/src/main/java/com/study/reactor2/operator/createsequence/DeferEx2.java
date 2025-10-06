package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class DeferEx2 {
    public static void main(String[] args) throws InterruptedException {
        Logger.log("start!!!!");

        /**
         * start!!!! | main | 20:57:56.183
         * Say Default Message | main | 20:57:56.366
         * onNext() | parallel-1 | 20:58:06.387 | Hello World
         *
         * 1. 시작하고 10초 후에 hello world 라는 데이터를 출력할 의도
         * 2. 하지만 default 값으로 null 일경우 switchIfEmpty 메소드를 사용해서 디폴트 메세지를 출력할 수 있는데
         * 3. 딜레이가 되는 동안에는 응답값이 없기 때문에 디폴트 메소드가 한번 호출되긴 하나, 결국 10초가 지나고, 해당 시점에 hello world 출력
         *
         * 여기서 주의할점은
         * 딜레이는 할 수 있으나, 느릴 경우 불필요하게 switchIfEmpty 를 출력할 수 있다.
         *
         * ### 해결 방법 ###
         * .switchIfEmpty(Mono.defer(() -> sayDefault()))
         * 로 변경하면 defer 자체 메서드로 감싼것은 는 구독 시점에 실행되기
         * 때문에 불필요하게 메소드를 실행하지 않고 hello world 출력 가능 (근거는 ex3 클래스 참조)
         */
        Mono.just("Hello World")
                .delayElement(Duration.ofSeconds(10))
                .switchIfEmpty(sayDefault())
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data))
        ;

        Thread.sleep(20000);
    }

    private static Mono<String> sayDefault() {
        Logger.log("Say Default Message");
        return Mono.just("Hi");
    }
}
