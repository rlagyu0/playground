package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureEx1 {
    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 10)
                // doOnNext : emit 하는 데이터를 볼 수 있음.
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT,  data))
                // doOnRequest : subcriber 에서 요청한 데이터의 갯수를 확인할 수 있음.
                .doOnRequest(data -> Logger.log(LogType.DO_ON_REQUEST,  data))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        Logger.log("# hookOnSubscribe() ============>  request call 2");
                        request(1);
                        // 데이터의 요청 갯수를 1로 지정한다.
                    }

                    @Override
                    protected void hookOnNext(Integer data) {
                        // 퍼블리셔 쪽에서 데이터를 emit 하면 해당 메소드 실행
                        // data 는 소비하는 데이터
                        Logger.log("# hookOnNext() ============> " + data + "   " + data % 3);
                        // 데이터 처리가 끝나면 request 1 로 퍼블리셔에 한개만 받는다고 알림
                        TimeUtil.sleep(2000L);
                        request(1);

                    }
                });

        Thread.sleep(20000L);
    }
}
