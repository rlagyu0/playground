package assertxxxx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class AssertEmptyTest {

    @Test
    public void assertEmptySuccessTest() {
        // 딜레이를 10초간 설정
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7)
                .delay(10000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation());

        TestObserver<Integer> test = observable.test();
        // awaitDone : 5초의 딜레이타임을 가지면서 통지되는 데이터의 지연을 5초간 딜레이 시킨다.
        // 5초까지 통지된 데이터가 존재하지 않으면 성공한다.
        test.awaitDone(5000L, TimeUnit.MILLISECONDS).assertEmpty();
    }

    @Test
    public void assertEmptyFailTest() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7)
                .subscribeOn(Schedulers.computation());

        TestObserver<Integer> test = observable.test();
        // awaitDone : 10초의 딜레이타임을 가지면서 통지되는 데이터의 지연을 10초간 딜레이 시킨다.
        // 10초까지 통지된 데이터가 존재하면 실패한다.
        test.awaitDone(10000L, TimeUnit.MILLISECONDS).assertEmpty();
    }
}
