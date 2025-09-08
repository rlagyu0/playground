package assertxxxx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertValuesTest {

    @Test
    public void assertValuesTest() {
        Observable.just("test", "test2", "test3", "test4").subscribeOn(Schedulers.computation())
                .filter(data -> !data.equals("test"))
                .test()
                .awaitDone(1000L, TimeUnit.MILLISECONDS)
                .assertValues("test2", "test3", "test4");
    }
}
