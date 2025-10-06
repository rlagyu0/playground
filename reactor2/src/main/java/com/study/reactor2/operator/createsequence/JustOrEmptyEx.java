package com.study.reactor2.operator.createsequence;

import reactor.core.publisher.Mono;

import java.util.Optional;

public class JustOrEmptyEx {
    public static void main(String[] args) {
        // emit 한 데이터가 null 이 아닐 경우 해당 데이터를 emit 하는 mono 를 생성한다.
        // 만약에 emit 할 데이터가 null 이면 onComplete 시그널을 발생시킨다.
        Mono
//                .justOrEmpty(null) 및 .justOrEmpty(Optional.ofNullable(null)) 로 대응이 가능하다.
                .justOrEmpty(Optional.ofNullable(null))
//                .just(null) 일반적으로 reactor 에서는 null 의 emit을 허용하지 않는다.
                .log()
                .subscribe();
        /**
         * 18:13:37.034 [main] INFO reactor.Mono.Empty.1 -- onSubscribe([Fuseable] Operators.EmptySubscription)
         * 18:13:37.038 [main] INFO reactor.Mono.Empty.1 -- request(unbounded)
         * 18:13:37.038 [main] INFO reactor.Mono.Empty.1 -- onComplete()
         */


    }
}
