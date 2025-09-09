package isxxxx;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class IsTerminatedTest {
    // 완료 통지 이벤트가 발생해서 종료 되었는지 여부를 검증하는 예제
    // 2.x 이후 없어짐
    // hasSubscription : 구독이 발생했는지 여부 ..
    // isDispose 등등 다른 검증 메소드들이 많다..
    @Test
    public void test1() {
//        Observable.interval(200L, TimeUnit.MILLISECONDS)
//                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
//                .take(5)
//                .doOnComplete(() -> Logger.log(LogType.DO_ON_COMPLETE))
//                .doOnError(e -> Logger.log(LogType.ON_ERROR, e.getMessage()))
//                .test()
//                .awaitCount(5)
//                .isTerminated();
    }
}
