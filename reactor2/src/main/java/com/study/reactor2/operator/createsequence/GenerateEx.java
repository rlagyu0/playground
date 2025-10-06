package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

/**
 * Generate 개념 이해
 * !! 동기적인 방식으로 데이터를 하나씩 순차적으로 emit 할때 사용한다 !!
 * !! 비동기적으로 생성하기 위해서는 create 참조 !!
 *
 * 파라미터
 *  1. 초기 상태 및 객체를 제공한다.
 *  2. signal 을 생성한다.
 *  3. generate 종료나, 구독 취소 시 호출되어 후처리 작업을 진행한다.
 */
public class GenerateEx {
    public static void main(String[] args) {

        /**
         * onNext() | main | 21:46:10.608 | 2 * 1 = 2
         * onNext() | main | 21:46:10.611 | 2 * 2 = 4
         * onNext() | main | 21:46:10.611 | 2 * 3 = 6
         * onNext() | main | 21:46:10.611 | 2 * 4 = 8
         * onNext() | main | 21:46:10.612 | 2 * 5 = 10
         * onNext() | main | 21:46:10.612 | 2 * 6 = 12
         * onNext() | main | 21:46:10.612 | 2 * 7 = 14
         * onNext() | main | 21:46:10.612 | 2 * 8 = 16
         * onNext() | main | 21:46:10.612 | 2 * 9 = 18
         */

        Flux.generate(
                // 1번째 파라미터 (초기 리소스나 상태의 제공)
                () -> Tuples.of(2,1),

                // 2번째 파라미터 (시그널을 발생시켜 넘길지 종료할지 정하는 구간)
                (state, sink) -> {
                    sink.next(state.getT1() + " * " + state.getT2() + " = " + state.getT1() * state.getT2());
                    if(state.getT2() == 9) {
                        sink.complete();
                    }
                    return Tuples.of(state.getT1(), state.getT2() + 1);
                },

                // 3번째 파라미터 (generate 종료나 구독취소같은 후처리 작업을 위함)
                state -> Logger.log("구구단 종료! : " + state.getT1() + "단")
        ).subscribe(data -> Logger.log(LogType.ON_NEXT, data));
    }
}
