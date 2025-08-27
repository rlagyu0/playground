package com.study.rxjava.transform;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Map;

public class ToMap {
    public static void main(String[] args) {
        // 생산자가 통지한 데이터를 map 형태로 변경한다.
        // key, value 쌍으로 map 형태로 반환
        // 생산자가 onSuccess 를 콜할때까지 기다렸다가 마지막에 맵을 전달
        // 마찬가지로 single 에 반환한다.
        Single<@NonNull Map<String, String>> map = Observable.just("a-Alpha",
                        "b-Base", // 이미 사용하고 있는 키를 똑같은 키로 설정할 경우 그 값을 덮는다.
                        "b-Bravo", "c-Chrlie", "e-Echo")
                .toMap(data -> data.split("-")[0]);

        Map<String, String> returnMap = map.blockingGet();
        System.out.println("returnMap = " + returnMap);

        map.subscribe(data -> Logger.log(LogType.ON_NEXT, data));

        // 사용법 2
        // 파라미터 첫번째가 key 값이 되고 value 도 지정할 수 있다.
        map = Observable.just("a-Alpha",
                        "b-Base", // 이미 사용하고 있는 키를 똑같은 키로 설정할 경우 그 값을 덮는다.
                        "b-Bravo", "c-Chrlie", "e-Echo")
                .toMap(data -> data.split("-")[0],
                             data -> data.split("-")[1]);

        map.subscribe(data -> Logger.log(LogType.ON_NEXT, data));


    }
}
