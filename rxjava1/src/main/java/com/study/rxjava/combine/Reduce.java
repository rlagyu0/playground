package com.study.rxjava.combine;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class Reduce {
    public static void main(String[] args) {
        // map, reduce 하둡
        Observable.just(1,2,3,4,5,6,7,8,9,10)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
//                .reduce((x,y) -> x+y)
                .reduce(0, (x,y) -> x+y)

                .subscribe(result -> Logger.log(LogType.ON_NEXT, result));


        //                .reduce((x,y) -> x+y)
        //                .reduce(0, (x,y) -> x+y)
        // 둘의 차이점 첫번재는 누적된 데이터를 계산하는 반면
        // 두번째는 첫번째 값을 내가 셋팅해줄 수 있다는 것 , -> 초기값은 0

        // 스캔이라는 함수도 있는데 reduce 는 합성된 최종 결과를 반환하지만
        // 스캔은 한번씩 연산될 때마다 통지를 한다.
    }
}
