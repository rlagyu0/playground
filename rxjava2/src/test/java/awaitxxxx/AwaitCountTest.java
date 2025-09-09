package awaitxxxx;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AwaitCountTest {
    /**
     * awaitCount : 파라미터로 지정된 개수만큼 통지될 때 까지 쓰레드를 대기시킨다.
     */
    @Test
    public void test() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                .take(5)
                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
                .doOnError(e -> Logger.log(LogType.DO_ON_ERROR, e.getMessage()))
                .test()
                .awaitCount(3) // 3개가 통지될 때 까지 스레드를 대기시키는 함수
                .assertNotComplete()
                .assertValueCount(3)
                .assertValues(0L, 1L, 2L);
    }
}
