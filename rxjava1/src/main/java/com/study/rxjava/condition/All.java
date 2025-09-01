package com.study.rxjava.condition;

import com.study.rxjava.filter.Filter;
import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class All {
    public static void main(String[] args) {
        // Observable 의 통지된 모든 데이터가 조건을 충족할 때 true 아니면 false 를 출력함
        // 반환값은 true/false 를 Single 타입으로 반환한다.
        // 통지된 데이터가 조건에 맞지 않는다면 이후 데이터는 구독 해지되어 통지 되지 않는다.
        List<Filter.Car> carList = Arrays.asList(
                new Filter.Car("CHEVOLET", "Car1"),
                new Filter.Car("HYUNDAI", "Car2"),
                new Filter.Car("HYUNDAI", "Car3"),
                new Filter.Car("BMW", "Car4"),
                new Filter.Car("KIA", "Car5"),
                new Filter.Car("BENTZ", "Car6"),
                new Filter.Car("CHEVOLET", "Car7"),
                new Filter.Car("CHEVOLET", "Car8"));

        Observable.fromIterable(carList)
                .doOnNext(car -> Logger.log(LogType.DO_ON_NEXT,
                        "Car Maker : " + car.getBrand() + ", Car Name : " + car.getName()))
                .map(car -> car.getBrand())
                .all(brand -> brand.equals("CHEVOLET"))
                .subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
