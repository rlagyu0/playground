package com.study.reactor.debug.debugmode;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

public class UseDebugModeHello {
    public static void main(String[] args) {

        /** Hooks.onOperatorDebug(); 실행 전
         *  subscribe :  | main | 23:35:24.440 | 2
         *  subscribe :  | main | 23:35:24.442 | 2
         *  subscribe :  | main | 23:35:24.442 | 2
         * / by zero | main | 23:35:24.447 | java.lang.ArithmeticException: / by zero
         */

        /** Hooks.onOperatorDebug(); 실행 후
         *
         */

        Hooks.onOperatorDebug();
        Flux.just(2,4,6,8)
                .zipWith(Flux.just(1,2,3,0), (x, y) -> x / y)
                .subscribe(data -> Logger.log( " subscribe : ", data),
                        err -> Logger.log(err.getMessage(), err));
    }
}
