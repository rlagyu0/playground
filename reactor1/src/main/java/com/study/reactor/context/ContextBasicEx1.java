package com.study.reactor.context;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ContextBasicEx1 {
    public static void main(String[] args) {
        String key = "message";

        Mono<String> mono = Mono.deferContextual(ctx -> {
                    return Mono.just("Hello " + ctx.get(key))
                            .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data));
                })
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .transformDeferredContextual((mono2, ctx) -> {
                    return mono2.map(data -> data + " " + ctx.get(key));
                })
                .contextWrite(context -> context.put(key, "Reactor"));

        mono.subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        TimeUtil.sleep(5000);
    }
}
