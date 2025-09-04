package blocking;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BlockingSubscribeTest {

    @Test
    public void blockingSubscribeTest() {
        AtomicReference<Long> sum = new AtomicReference<>(0L);
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .take(14)
                .filter(data -> data > 5 && data < 11)
                .blockingSubscribe(data -> {
                    sum.updateAndGet(value -> value + data);
                });

        Assertions.assertEquals(40, sum.get());
    }
}
