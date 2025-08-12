package com.study.rxjava.hello;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ToDoSample {
    public static void main(String[] args) throws InterruptedException {
        Observable.just(100,200,300,400,500)
            .doOnNext(number -> System.out.println(getThreadName() + " doOnNext number = " + number))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .filter(number -> number > 200)
            .subscribe(number -> System.out.println(getThreadName() + " number = " + number));

        Thread.sleep(1000);
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
