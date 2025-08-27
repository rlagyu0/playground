package com.study.rxjava.transform;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class SwitchMap {
    public static void main(String[] args) throws InterruptedException {
        // concatMap 과 switchMap 은 둘다 순서를 보장하지만, concatMap 은 순서를 보장해서
        // 선행된 소비해야할 데이터가 존재하면 다음 소비되어야할 데이터가 대기하는데,
        // 만약 선행된 소비해야할 데이터가 처리중인데 다음 소비되어야할 데이터가 중간에 통지되면
        // 선행 처리되어지고 있던 데이터는 유실되고 그 중간에 다음 데이터가 처리가 진행된다.
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .skip(1)
                .take(10)
                /**
                 * doOnNext 에서 는 아래와 같이 1-10 까지 다 통지가 되는 것을 확인할 수 있다.
                 * 1,2,3,4,5,6,7,8,9,10
                 */
                .doOnNext(System.out::println)
                /**
                 * 하지만 스위치맵은 10단만 출력을 한다.
                 * 그렇다. 통지된 데이터의 속도가 너무 빨라
                 * 1 -> 1단을 출력해볼까? 1 *.... 엇 다음데이터(2) 통지
                 * 2 -> 2단을 출력해볼까? 2 *.... 엇 다음데이터(3) 통지
                 * 즉, 여기 까지는 다음 통지 데이터가 있기 때문에 구구단을 출력할 시간 조차 주지 않는다.
                 *
                 * 10 -> 10단을 출력해볼까?
                 * onNext() | RxComputationThreadPool-11 | 01:03:41.759 | 10 * 1 = 10
                 * onNext() | RxComputationThreadPool-11 | 01:03:41.950 | 10 * 2 = 20
                 * onNext() | RxComputationThreadPool-11 | 01:03:42.150 | 10 * 3 = 30
                 * onNext() | RxComputationThreadPool-11 | 01:03:42.350 | 10 * 4 = 40
                 * onNext() | RxComputationThreadPool-11 | 01:03:42.551 | 10 * 5 = 50
                 * onNext() | RxComputationThreadPool-11 | 01:03:42.752 | 10 * 6 = 60
                 * onNext() | RxComputationThreadPool-11 | 01:03:42.951 | 10 * 7 = 70
                 * onNext() | RxComputationThreadPool-11 | 01:03:43.151 | 10 * 8 = 80
                 * onNext() | RxComputationThreadPool-11 | 01:03:43.351 | 10 * 9 = 90
                 * 즉, 마지막 데이터 10이 통지되고 다음 데이터가 없기 때문에 끊길 일도 없다.
                 *
                 */
                .switchMap(number -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(10)
                        .skip(1)
                        .map(row -> number + " * " + row + " = " + row * number))
                .subscribe(
                        data -> Logger.log(LogType.ON_NEXT, data.toString()),
                        error -> {},
                        () -> {
                            TimeUtil.end();
                            TimeUtil.takeTime();
                        }
                );
        Thread.sleep(40000);
    }
}
