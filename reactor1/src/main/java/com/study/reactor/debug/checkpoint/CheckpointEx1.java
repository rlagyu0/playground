package com.study.reactor.debug.checkpoint;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

public class CheckpointEx1 {
    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        Flux.just(2,4,6,8)
                .zipWith(Flux.just(1,2,3,0), (x, y) -> x / y)
                .checkpoint() // 에러가 예상되는 곳에 checkpoint
                                        // 여기는 우리가 눈으로 봤을 때 에러가 날게 뻔해서 적용했지만,
                                        // 에러가 나는 곳을 모른다라고 한다면
                .map(num -> num + 2)
                .checkpoint() // 두개를 쓴다면, zipwith 에서 에러가 날 시 checkpoint 가 2개 생성
                                        //  map 에서 에러가 난다면 checkpoint 1개 생성
                .subscribe(data -> Logger.log( " subscribe : ", data),
                        err -> Logger.log(err.getMessage(), err));

        /**
         * .checkpoint(문자열) 을 지정하면
         * 우리의 에러가 발생한 지점에 특정 문자열의 문구가 들어가서 로깅된다. (체크포인트 생성)
         */
    }
}
