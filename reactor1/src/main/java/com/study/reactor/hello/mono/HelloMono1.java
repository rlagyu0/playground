package com.study.reactor.hello.mono;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Mono;

public class HelloMono1 {
    public static void main(String[] args) {
        Mono.just("Hello")
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
