package com.study.reactor.hello.flux;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;

public class HelloFlux2 {
    public static void main(String[] args) {
        Flux.fromArray(new Integer[]{3,6,7,9})
                .filter(num -> num > 6)
                .map(num -> num * 2)
                .subscribe(multiply -> Logger.log(LogType.ON_NEXT, multiply));
    }
}
