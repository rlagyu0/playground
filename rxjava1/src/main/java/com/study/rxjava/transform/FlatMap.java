package com.study.rxjava.transform;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class FlatMap {
    // 원본 Observable 에서 통지하는 데이터를 원하는 값으로 변환 후 통지
    // 변환 전 후 데이터 타입은 달라도 상관 없다.
    // 주의할 점은 null 을 반환하면 안된다.
    public static void main(String[] args) {
        // 1. Map 함수
        // 1개를 전달받아 한개를 변환하여 전달함
        List<Integer> oddList = Arrays.asList(1, 3, 5, 7);
        Observable.fromIterable(oddList)
                .map(num -> "1을 더한 결과 " + (num + 1))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        // 2. FlatMap
        // 1개를 전달받아 여러개를 전달함 (새로운 타임라인을 전달한다 Observable)
        // 1 -> (1,2) | 2-> (2,3) ...
        // 1, 2, 2, 3 라는 타임라인으로 전달.
        /**
         * onNext() | main | 00:35:48.083 | hello , 자바
         * onNext() | main | 00:35:48.084 | hello , 파이썬
         * onNext() | main | 00:35:48.084 | hello , 안드로이드
         * onNext() | main | 00:35:48.084 | bye , 자바
         * onNext() | main | 00:35:48.084 | bye , 파이썬
         * onNext() | main | 00:35:48.084 | bye , 안드로이드
         */
        Observable.just("hello", "bye")
                .flatMap(hello -> Observable.just("자바", "파이썬", "안드로이드")
                .map(lang -> hello + " , " + lang))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        // step 1 => 그럼 구구단도 출력이 가능해 -> 6단까지만 출력하시오
        Observable.range(1, 6)
                .flatMap(number -> Observable.range(1, 9).map(subNumber -> number + " * " + subNumber + " = " + subNumber * number))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));

        // step 2 =>  이어서 쓰기 너무 어려우니 flatMap 의 또다른 사용법
        Observable.range(1, 6)
                // 즉 , 1 - 6 까지의 데이터를 변환할 건데 (스텝 1이 1에 대해 (1,2,3,4,... 9) 의 타임라인을 반환했다면)
                .flatMap(number -> Observable.range(1, 9),
                // 이번에는 1에 대해 1,2,3,4,5... 9 를 조합해서 반환 가능
                (number, subNumber) ->  number + " * " + subNumber + " = " + subNumber * number)
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data.toString()));




    }
}
