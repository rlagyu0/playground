package awaitxxxx;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AwaitTest {

    /**
     * await : 1. 생산자 쪽에서 완료 통지나 에러 통지가 있을 때 까지 스레드를 대기시킨다. (파라미터가 없는 경우)
     *             2. 파라미터로 지정된 시간동안 대기하며, 대기 시간내에 완료 통지가 있었는지 여부(true/false) 검증. (파라미터가 있는 경우)
     */
    @Test
    public void test() throws InterruptedException {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .take(5)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
                .doOnError(error -> Logger.log(LogType.ON_ERROR, error.getMessage()))
                .test()
                .await() // 완료나 에러 통지가 존재할때 까지 스레드를 무한정 대기함.
                .assertComplete()
                .assertValueCount(5);
    }

    @Test
    public void test2() throws InterruptedException {
        boolean await = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .take(5)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
                .doOnError(error -> Logger.log(LogType.ON_ERROR, error.getMessage()))
                .test()
                .await(2000L, TimeUnit.MILLISECONDS);
        //        파라미터로 지정된 시간동안 대기하며, 대기 시간내에 완료 통지가 있었는지 여부(true/false) 검증.
        // 0.1 초 간격으로 5개를 0.5초안에 발행하고 2초 이내에 완료 통지가 있었으므로 true 를 반환한다.
        Assertions.assertTrue(await);

    }
}
