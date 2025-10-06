package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Flux;

import java.util.Arrays;

public class FromIterableEx {
    public static void main(String[] args) {
        // 파라미터로 튜플도 이용을 많이함.
        Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5))
                .subscribe(data -> Logger.log( LogType.ON_NEXT, data));
    }

}
