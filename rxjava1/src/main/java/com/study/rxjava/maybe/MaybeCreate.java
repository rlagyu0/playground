package com.study.rxjava.maybe;

import com.study.rxjava.util.DateUtil;
import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;

public class MaybeCreate {
    public static void main(String[] args) {
        Maybe<String> maybe = Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> emitter) throws Exception {
                // 데이터 통지가 있을 경우
                // emitter.onSuccess(DateUtil.getNowDate());
                // 데이터 통지가 없을 경우 호출
                emitter.onComplete();
            }
        });

        maybe.subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                // 아무것도 하지 않음.
            }

            @Override
            public void onSuccess(String data) {
                Logger.log(LogType.ON_SUCCESS, "# 현재 날짜시각: " + data);
            }

            @Override
            public void onError(Throwable error) {
                Logger.log(LogType.ON_ERROR, error);
            }

            @Override
            public void onComplete() {
                Logger.log(LogType.ON_COMPLETE);
            }
        });

//        Maybe<String> maybe = Maybe.create(emitter -> {
//            emitter.onSuccess(DateUtil.getNowDate());
////            emitter.onComplete();
//        });
//
//        maybe.subscribe(
//                data -> Logger.log(LogType.ON_SUCCESS, "# 현재 날짜시각: " + data),
//                error -> Logger.log(LogType.ON_ERROR, error),
//                () -> Logger.log(LogType.ON_COMPLETE)
//        );
    
    }
}
