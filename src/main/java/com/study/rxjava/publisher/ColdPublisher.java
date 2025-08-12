package com.study.rxjava.chapter2;

import io.reactivex.rxjava3.core.Flowable;

public class ColdPublisher {

    public static void main(String[] args) {
        Flowable<Integer> flowable = Flowable.just(1, 3, 5, 7, 9);

        flowable.subscribe(number -> System.out.println("Subscriber 1: " + number));
        flowable.subscribe(number -> System.out.println("Subscriber 2: " + number));
    }
}
