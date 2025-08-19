package com.study.rxjava.single;

import com.study.rxjava.util.DateUtil;
import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;

public class SingleCreate {
    public static void main(String[] args) {
        Single<String> single = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                emitter.onSuccess(DateUtil.getNowDate());
                // onNext, onComplete 메서드는 사용하지 않는다.
                // 대체하는 함수이다.
            }
        });

        single.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                // 아무것도 하지 않음.
                // 데이터를 한건만 받기 때문에 아무것도 하지 않는다.
                // 데이터를 소비할 준비가 되면 onSubscribe 메서드가 호출된다.
            }

            @Override
            public void onSuccess(String data) {
                Logger.log(LogType.ON_SUCCESS, "# 날짜시각: " + data);
            }

            @Override
            public void onError(Throwable error) {
                Logger.log(LogType.ON_ERROR, error);
            }
        });

//        Single<String> single = Single.create(emitter -> emitter.onSuccess(DateUtil.getNowDate()));
//
//        single.subscribe(
//                data -> Logger.log(LogType.ON_SUCCESS, "# 날짜시각: " + data),
//                error -> Logger.log(LogType.ON_ERROR, error)
//        );
    }
}
