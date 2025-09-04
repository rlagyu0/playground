package blocking;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlockingSingleTest {

    // 생산자가 한개의 데이터를 통지하고 완료되면 해당 데이터를 반환
    // 2개 이상의 데이터를 통지할 경우에 IllegalArgumentException
    @Test
    public void blockingSingleTest() {
        Integer actual = Observable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.computation())
                .filter(data -> data > 4)
                .blockingSingle();

        Assertions.assertEquals(5, actual);
    }

    @Test
    public void blockingSingleExceptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Observable.just(1, 2, 3, 4, 5)
                    .subscribeOn(Schedulers.computation())
                    .filter(data -> data > 3)
                    .blockingSingle();
        });
    }
}
