package com.study.rxjava.filter;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class Distinct {

    record Car(String brand) {
        public String getBrand() {
            return brand;
        }
    }

    public static void main(String[] args) {
        List<Distinct.Car> carList = Arrays.asList(
                new Distinct.Car("CHEVOLET"),
                new Distinct.Car("HYUNDAI"),
                new Distinct.Car("HYUNDAI"),
                new Distinct.Car("BMW"),
                new Distinct.Car("KIA"),
                new Distinct.Car("BENTZ"),
                new Distinct.Car("CHEVOLET"),
                new Distinct.Car("CHEVOLET"));

        Observable.fromIterable(carList)
                .distinct(car -> car.getBrand() )
                .subscribe(car -> Logger.log(LogType.ON_NEXT, car.toString()));
    }
}
