package assertxxxx;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertNoValuesTest {
    @Test
    public void assertNoValuesTest() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, + data))
                .filter(data -> data > 5)
                .test()
                .awaitDone(100L, TimeUnit.MILLISECONDS)
                .assertNoValues();
    }
}
