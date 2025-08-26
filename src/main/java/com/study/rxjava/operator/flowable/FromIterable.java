package com.study.rxjava.operator.flowable;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class FromIterable {
    public static void main(String[] args) {
        // iterable 을 받아서 Observable 을 생성한다.
        List<String> list = Arrays.asList("Kor", "USA", "Japan");
        Observable.fromIterable(list).subscribe(country -> Logger.log(LogType.ON_NEXT, country));
    }
}
