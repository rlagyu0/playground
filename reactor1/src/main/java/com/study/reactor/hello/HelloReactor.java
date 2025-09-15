package com.study.reactor.hello;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;

public class HelloReactor {
    public static void main(String[] args) {
        Flux<String> sequence = Flux.just("Hello", "World");
        sequence.map(String::toLowerCase)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
