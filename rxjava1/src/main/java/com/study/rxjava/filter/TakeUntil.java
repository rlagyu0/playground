package com.study.rxjava.filter;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TakeUntil {
    public static void main(String[] args) throws InterruptedException {
        List<Filter.Car> carList = Arrays.asList(
                new Filter.Car("CHEVOLET", "Car1"),
                new Filter.Car("HYUNDAI", "Car2"),
                new Filter.Car("HYUNDAI", "Car3"),
                new Filter.Car("BMW", "Car4"),
                new Filter.Car("KIA", "Car5"),
                new Filter.Car("BENTZ", "Car6"),
                new Filter.Car("CHEVOLET", "Car7"),
                new Filter.Car("CHEVOLET", "Car8"));

        // 1. KIA 가 나올때 까지 (KIA 포함)
        Observable.fromIterable(carList)
                .takeUntil((Predicate<? super Filter.Car>) data -> data.getBrand().equals("KIA"))
                .subscribe(car -> System.out.println("car.getName = " + car.getName()));

        // 2. 5초 까지만 나온 데이터가 출력 됨.
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .takeUntil(Observable.timer(5500L, TimeUnit.MILLISECONDS))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        Thread.sleep(5500);


    }
}
