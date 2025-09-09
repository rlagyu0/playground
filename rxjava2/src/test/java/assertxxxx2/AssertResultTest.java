package assertxxxx2;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertResultTest {

    // 해당 시점까지 통지를 완료했고, 통지된 데이터와 파라미터로 입력된 데이터의 값과 순서가 같으면 테스트에 성공한다.
    // assetrValues와 다른점은 특정 시점까지 완료 통지를 받았는지 않받았는지가 관건이다.


    // 성공 예제
    @Test
    public void assertResultTestV2() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.ON_NEXT, data))
                .take(5)
                .filter(data -> data > 3)
                .test()
                .awaitDone(1100L, TimeUnit.MILLISECONDS)
                .assertResult(4L);
    }

    // 테스트 실패 예제
    // 완료 통지가 없으며 무한정으로 발행하고 있어서 실패하는 테스트 케이스
    // assertValues 와 다른점은 완료 통지가 있어야지만 검증이 완료됨

    /**
     * onNext() | RxComputationThreadPool-1 | 22:53:44.215 | 0
     * onNext() | RxComputationThreadPool-1 | 22:53:44.398 | 1
     * onNext() | RxComputationThreadPool-1 | 22:53:44.599 | 2
     * onNext() | RxComputationThreadPool-1 | 22:53:44.799 | 3
     * onNext() | RxComputationThreadPool-1 | 22:53:44.999 | 4
     *
     * java.lang.AssertionError: Not completed (latch = 1, values = 1, errors = 0, completions = 0, timeout!, disposed!)
     */
    @Test
    public void assertResultTest() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.ON_NEXT, data))
                .filter(data -> data > 3)
                .test()
                .awaitDone(1100L, TimeUnit.MILLISECONDS)
                .assertResult(4L);
    }

}
