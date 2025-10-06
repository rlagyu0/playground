package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Flux;

public class RangeEx {
    public static void main(String[] args) {
        // start 는 3부터, 5까지의 뜻이 아님
        // ** 3부터 5개를 순차적으로 가져오겠다는 거임
        //        onNext() | main | 18:33:30.979 | 3
        //        onNext() | main | 18:33:30.981 | 4
        //        onNext() | main | 18:33:30.982 | 5
        //        onNext() | main | 18:33:30.982 | 6
        //        onNext() | main | 18:33:30.982 | 7
        Flux.range(3, 5)
                .subscribe(data -> Logger.log( LogType.ON_NEXT, data));
    }
}
