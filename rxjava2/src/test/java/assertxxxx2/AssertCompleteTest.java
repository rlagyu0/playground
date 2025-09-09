package assertxxxx2;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertCompleteTest {
    @Test
    public void assertCompleteTest() {
        Observable.interval(200L, TimeUnit.MILLISECONDS)
                .take(9)
                .test()
                .awaitDone(1000L, TimeUnit.MILLISECONDS)
                .assertComplete();

    }
}
