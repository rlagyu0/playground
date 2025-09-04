package blocking;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class BlockingForEachTest {
    @Test
    public void blockingForEachTest() {

        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .take(14)
                .filter(data -> data > 5 && data < 11)
                .blockingForEach(data -> {
                    System.out.println("data = " + data);
                    Assertions.assertTrue(data > 5);
                });
    }
}
