package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public class FromStream {
    public static void main(String[] args) {
        // FromIterable 과 유사한 기능을 제공하나, 단지 스트림을 파라미터로 Flux 를 생성한다
        // * stream 은 재사용이 불가능해 cancel, complete, error 등의 시그널이 발생하면 스트림 재사용이 불가능한다.
        Flux.fromStream(Stream.of(1, 2, 3, 4, 5))
                .log()
                .subscribe(data -> Logger.log( LogType.ON_NEXT, data));
    }
}
