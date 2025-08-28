package com.study.rxjava.error;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;

public class CanNotUseTryCatchExample1 {
    public static void main(String[] args) {
        // 일반적으로 try catch 문에서 rxjava 의 에러를 처리할 수 없음
        // Caused by: java.lang.ArithmeticException: / by zero 해당 에러를 발생시키는 코드를 실행하면
        // 아래와 같이 에러가 하나더 나온다.
        // The exception was not handled due to missing onError handler in the subscribe() method call. Further reading:
        // 에러 핸들러가 없어서 처리할 수 없다는 메세지
        try {
            Observable.just(2)
                    .map(num -> num / 0) // 에러를 일부러 발생
                    .subscribe(System.out::println);
        }catch (Exception e){
            Logger.log(LogType.ON_ERROR, "에러 처리가 필요" + e.getCause());
        }
    }
}
