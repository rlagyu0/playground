package com.study.reactor.debug.log;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class LogEx1 {
    public static Map<String, String> fruits = new HashMap<>();

    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("grape", "포도");
    }

    public static void main(String[] args) {

       Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .log()
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length()-1))
                .map(fruit -> fruits.get(fruit))
                .subscribe(data -> Logger.log(LogType.DO_ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error));

        Hooks.onOperatorDebug();

        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .log()
                .map(String::toLowerCase)
                .log()
                .map(fruit -> fruit.substring(0, fruit.length() - 1))
                .log()
                .map(fruit -> fruits.get(fruit))
                .log()
                .subscribe(data -> Logger.log(LogType.DO_ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error));

        Hooks.onOperatorDebug();
        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .subscribeOn(Schedulers.boundedElastic())
                .log("Fruit.Source")
                .publishOn(Schedulers.parallel())
                .map(String::toLowerCase)
                .log("Fruit.Lower")
                .map(fruit -> fruit.substring(0, fruit.length() - 1))
                .log("Fruit.Substring")
                .map(fruit -> fruits.get(fruit))
                .log("Fruit.Name")
                .subscribe(data -> Logger.log(LogType.DO_ON_NEXT, data),
                        error -> Logger.log(LogType.ON_ERROR, error));

        TimeUtil.sleep(4000);
    }
}
