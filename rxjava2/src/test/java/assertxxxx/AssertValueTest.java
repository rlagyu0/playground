package assertxxxx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertValueTest {

    @Test
    public void assertValueTest() {
        Observable.just("a").test().assertValue("a");
    }

    @Test
    public void assertValueTestV2() {
        Observable.just("test", "test2", "test3", "test4").subscribeOn(Schedulers.computation())
                .filter(data -> data.equals("test"))
                .test()
                .awaitDone(1000L, TimeUnit.MILLISECONDS)
                .assertValue("test");
    }
}
