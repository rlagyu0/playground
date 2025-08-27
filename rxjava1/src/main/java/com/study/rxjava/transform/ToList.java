package com.study.rxjava.transform;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public class ToList {
    public static void main(String[] args) {
        // 생산자로부터 전달받은 데이터를 모두 List 에 담아서 반환한다.
        // 생산자로부터 전달이 다 끝나야 리스트를 통지함.
        // 1 통지 , 2 통지 , 3 통지 -> [1,2,3]
        // [1,2,3] 은 원본데이터를 담은 리스트 하나이기 때문에 Single 로 반환한다.
        Single<@NonNull List<Integer>> list = Observable.just(1, 3, 5, 7, 9).toList();

        //        Single<T>는 비동기적으로 하나의 값(또는 에러)을 방출하는 스트림이죠.
        //
        //        blockingGet()을 호출하면,
        //        현재 쓰레드(호출한 쓰레드)에서 Single의 실행을 구독(subscribe) 하고
        //        onSuccess(T) 또는 onError(Throwable) 이벤트가 발생할 때까지 블로킹(대기) 합니다.
        //        값이 방출되면 그 값을 리턴하고, 에러가 나면 예외를 던집니다.
        //                즉, 비동기 → 동기 변환입니다.
        List<Integer> integers = list.blockingGet();

        // 위와 같은 방식이나, 내가 직접 처리할 때 구독
        list.subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
