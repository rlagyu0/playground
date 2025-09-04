package blocking;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class BlockingIterableTest {

    @Test
    public void blockingIterableTest1() {
        Iterator<Long> iterator = Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .take(5)
                .blockingIterable()
                .iterator();

        Assertions.assertTrue(iterator.hasNext());
        int actual0 = iterator.next().intValue();
        System.out.println("actual0 = " + actual0);
        Assertions.assertEquals(0, actual0);
        int actual1 = iterator.next().intValue();
        System.out.println("actual1 = " + actual1);
        Assertions.assertEquals(1, actual1);
        int actual2 = iterator.next().intValue();
        System.out.println("actual2 = " + actual2);
        Assertions.assertEquals(2, actual2);
        int actual3 = iterator.next().intValue();
        System.out.println("actual3 = " + actual3);
        Assertions.assertEquals(3, actual3);
        int actual4 = iterator.next().intValue();
        System.out.println("actual4 = " + actual4);
        Assertions.assertEquals(4, actual4);

    }
}
