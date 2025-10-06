package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import com.study.reactor2.util.TimeUtil;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class DeferEx1 {
    public static void main(String[] args) {
        // 구독하는 시점에 데이터를 emit 한다.

        // 즉, 데이터를 emit 지연시키는것으로 인해서 꼭 필요한 시점에 데이터를 emit 하여
        // 불필요한 프로세스를 줄일 수 있다.

        /**
         * 설명 :
         * 다운스트림에서 구독을 진행하면 업스트림에서도 구독이 진행된다.
         * <p>
         *  <img class="marble" src="doc-files/marbles/deferForMono.svg" alt="">
         * <p>
         */

        Logger.log("start!!");

        // 일반 just 로 생성할때는 생성 시점의 날짜, 시간이 데이터로 정의된다. 후에 어떤 구독이 계속 호출되더라도 똑같은 데이터가 나온다.
        Mono<LocalDateTime> just = Mono.just(LocalDateTime.now());
        // 반면에 defer 는 구독 시점의 날짜 즉, 구독할 때 마다 다른 시간의 데이터가 추출된다.
        // 즉, 매번 다른 데이터가 나올 수 있다는 뜻.
        Mono<LocalDateTime> defer = Mono.defer(() -> Mono.just(LocalDateTime.now()));

        TimeUtil.sleep(2000);

        just.subscribe(data -> Logger.log(LogType.ON_NEXT, "just1" + data));
        defer.subscribe(data -> Logger.log(LogType.ON_NEXT, "defer1" + data));
        /**
         * onNext() | main | 20:34:09.513 | just12025-10-06T20:34:07.335096200
         * onNext() | main | 20:34:09.517 | defer12025-10-06T20:34:09.517572400
         */

        TimeUtil.sleep(2000);

        just.subscribe(data -> Logger.log(LogType.ON_NEXT, "just2" + data));
        defer.subscribe(data -> Logger.log(LogType.ON_NEXT, "defer2" + data));
    }


}
