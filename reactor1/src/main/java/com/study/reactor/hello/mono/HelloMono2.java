package com.study.reactor.hello.mono;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Mono;

public class HelloMono2 {
    public static void main(String[] args) {
        Mono.empty()
                .subscribe(
                data -> Logger.log("emit data {}", data),
                error -> {},
                () -> Logger.log("emit on complete signal")
        );
    }
}
