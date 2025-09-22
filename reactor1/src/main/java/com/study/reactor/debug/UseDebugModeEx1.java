package com.study.reactor.debug;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.HashMap;
import java.util.Map;

public class UseDebugModeEx1 {
    public static Map<String, String> fruits = new HashMap<>();

    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "배");
        fruits.put("grape", "포도");
        fruits.put("apple", "사과");
    }

    public static void main(String[] args) {
        Hooks.onOperatorDebug();

        Flux.fromArray(new String[] {"BANANAS", "APPLE", "PEARS, MELONS"})
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length() -1))
                .map(fruits::get)
                .map(translated -> "맛있는 " + translated)
                .subscribe(data -> Logger.log(data),
                        err -> Logger.log(err.getMessage(), err));
    }
}



