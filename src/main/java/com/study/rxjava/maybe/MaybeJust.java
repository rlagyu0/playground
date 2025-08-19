package com.study.rxjava.maybe;

import com.study.rxjava.util.DateUtil;
import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Maybe;

public class MaybeJust {
    public static void main(String[] args) {
        // 데이터 통지가 없는 경우 onComplete 만 수행
        //data -> Logger.log(LogType.ON_COMPLETE)
//        Maybe.empty()
//                .subscribe(
//                        data -> Logger.log(LogType.ON_SUCCESS, data),
//                        error -> Logger.log(LogType.ON_ERROR, error),
//                        () -> Logger.log(LogType.ON_COMPLETE)
//                );
//
        //데이터 통지가 있는 경우
        // Logger.log(LogType.ON_SUCCESS, data), 수행

        Maybe.just(DateUtil.getNowDate())
        .subscribe(
                data -> Logger.log(LogType.ON_SUCCESS, "# 현재 날짜시각: " + data),
                error -> Logger.log(LogType.ON_ERROR, error),
                () -> Logger.log(LogType.ON_COMPLETE)
        );

    }
}
