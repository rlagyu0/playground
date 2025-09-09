package awaitxxxx;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AwaitDoneTest {
    @Test
    public void test() {
        /**
         * awaitDone : 파라미터로 지정된 시간동안 스레드를 대기시키거나
         *                      지정된 시간 이전에 완료나 에러 통지가 있다면
         *                      통지가 있을 때 까지만 대기시킨다.
         *
         * await : 1. 생산자 쪽에서 완료 통지나 에러 통지가 있을 때 까지 스레드를 대기시킨다. (파라미터가 없는 경우)
         *             2. 파라미터로 지정된 시간동안 대기하며, 대기 시간내에 완료 통지가 있었는지 여부(true/false) 검증. (파라미터가 있는 경우)
         *
         * awaitCount : 파라미터로 지정된 개수만큼 통지될 때 까지 쓰레드를 대기시킨다.
         */
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .take(5)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
                .doOnError(e -> Logger.log(LogType.DO_ON_ERROR, e.getMessage()))
                .test()
                // 최대 지정된 시점 (0.5) 초까지 완료 통지가 올때까지 대기하고 완료통지가 존재하지 않아
                // 그동안 발행된 데이터가 2개 인지 검증하는 여부
                .awaitDone(500L, TimeUnit.MILLISECONDS)
                .assertNotComplete()
                .assertValueCount(2);
    }

    @Test
    public void test2() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .take(5)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
                .doOnError(e -> Logger.log(LogType.DO_ON_ERROR, e.getMessage()))
                .test()
                // 최대 지점인 1.5초가 되는 시점까지 0.2  초 간격으로 5개를 발행한다.
                // 그러면 1.5 초이내인 1초만에 작업이 완료되고 onComplete 가 콜될거고
                // assertValueCount로 5를 검증하니, 총 5개를 소비한것이 일치하여 패스
                .awaitDone(1500L, TimeUnit.MILLISECONDS)
                .assertComplete()
                .assertValueCount(5);
    }
}
