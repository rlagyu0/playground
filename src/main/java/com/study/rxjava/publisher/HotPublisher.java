package com.study.rxjava.publisher;

import io.reactivex.rxjava3.processors.PublishProcessor;

public class HotPublisher {
    public static void main(String[] args) {
        PublishProcessor<Object> processor = PublishProcessor.create();
        processor.subscribe(number -> System.out.println("Subscriber 1: " + number));
        processor.onNext(1);
        processor.onNext(2);

        processor.subscribe(number -> System.out.println("Subscriber 2: " + number));
        processor.onNext(3);
        processor.onNext(4);

        processor.onComplete();
    }
}
