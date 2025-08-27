package com.study.rxjava.transform;

import com.study.rxjava.filter.Filter;
import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;

import java.util.Arrays;
import java.util.List;

public class GroupBy {
    public static void main(String[] args) {
        List<Filter.Car> carList = Arrays.asList(
                new Filter.Car("CHEVOLET", "Car1"),
                new Filter.Car("HYUNDAI", "Car2"),
                new Filter.Car("HYUNDAI", "Car3"),
                new Filter.Car("BMW", "Car4"),
                new Filter.Car("KIA", "Car5"),
                new Filter.Car("BENTZ", "Car6"),
                new Filter.Car("CHEVOLET", "Car7"),
                new Filter.Car("CHEVOLET", "Car8"));

        Observable<GroupedObservable<String, Filter.Car>> groupedObservableObservable
                = Observable.fromIterable(carList).groupBy(Filter.Car::getBrand);

        groupedObservableObservable.subscribe(
                groupedObservable -> {
                    groupedObservable
                            .filter(data -> groupedObservable.getKey().equals("HYUNDAI"))
                            .doOnNext(observable -> Logger.log(LogType.ON_NEXT, observable.getBrand() + "로 그루핑 됨."))
                            .subscribe(
                            car -> {
                                Logger.log(LogType.ON_NEXT, groupedObservable.getKey() + " CarName = " + car.getName());
                            }
                    );
                }
        );
    }
}
