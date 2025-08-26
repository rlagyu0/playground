package com.study.rxjava.filter;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class Filter {
    record Car(String brand, String name) {
        public String getBrand() {
            return brand;
        }

        public String getName() {
            return name;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        List<Car> carList = Arrays.asList(
                new Car("CHEVOLET", "Car1"),
                new Car("HYUNDAI", "Car2"),
                new Car("HYUNDAI", "Car3"),
                new Car("BMW", "Car4"),
                new Car("KIA", "Car5"),
                new Car("BENTZ", "Car6"),
                new Car("CHEVOLET", "Car7"),
                new Car("CHEVOLET", "Car8"));

        Observable.fromIterable(carList)
                .filter(car -> car.getBrand().equals("HYUNDAI"))
                .filter(car -> car.getName().equals("Car2"))
                .subscribe(car -> System.out.println("car.getName = " + car.getName()));

        Thread.sleep(3000);
    }
}
