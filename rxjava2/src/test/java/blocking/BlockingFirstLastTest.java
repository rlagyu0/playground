package blocking;

import com.study.rxjava.util.LogType;
import com.study.rxjava.util.Logger;
import com.study.rxjava.util.TimeUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class BlockingFirstLastTest {
    @Test
    public void blockingFirstTest() {
        long start = System.currentTimeMillis();

        Long actual = Observable.interval(3000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .blockingFirst();

        long currentTime = TimeUtil.getCurrentTime();

        Logger.log(LogType.PRINT, "### 걸린 시간 : " + (currentTime - start) + "ms");
        Assertions.assertEquals(0, actual);
    }

    @Test
    public void blockingLastTest() {
        long start = System.currentTimeMillis();

        Long actual = Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .take(5)
                .subscribeOn(Schedulers.computation())
                .blockingLast();

        long currentTime = TimeUtil.getCurrentTime();

        Logger.log(LogType.PRINT, "### 걸린 시간 : " + (currentTime - start) + "ms");
        Assertions.assertEquals(4, actual);
    }
}
