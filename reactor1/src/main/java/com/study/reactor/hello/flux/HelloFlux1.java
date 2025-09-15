package com.study.reactor.hello.flux;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;

public class HelloFlux1 {
    public static void main(String[] args) {
        Flux.just(3,6,7)
                .map(i -> i % 2)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
