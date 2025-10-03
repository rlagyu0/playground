package com.study.reactor.test;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasLength;

/**
 * 📌 Backpressure 테스트
 * hasDropped(), hasDiscarded() 등을 이용해서 backpressure 테스트 수행 가능
 *
 * 📌 Context 테스트
 * expectAccessibleContext() : 접근 가능한 Context가 있는지 테스트
 * hasKey() : Context에 특정 key가 존재하는지 검증
 *
 * 📌 기록된 데이터를 이용한 테스트
 * recordWith() : emit 된 데이터를 기록
 * consumeRecordedWith() : 기록된 데이터를 소비하며 검증
 * expectRecordedMatches() : 기록된 데이터의 컬렉션을 검증
 */
public class StepVerifierBackPressureEx {
    @Test
    public void test1() {

        /**
         * 요청(1번) 데이터의 갯수보다 많은 데이터가 emit 되어지면 error 발생
         *  overflowexception 이 발생한 데이터는 discard (2번 데이터에서 exception)
         *  나머지 emit 된 데이터들은 Hooks.onnextdroped 를 통해 가로채서 drop 됨 (3부터는 다운스트림으로 emit 되지 않고 discard)
         */
        Flux<Integer> flux = Flux.create(emitter -> {
            for (int i = 1; i <= 100; i++) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.ERROR); // backpressure 전략으로 error 전략을 선택한다.

        StepVerifier.create(flux, 1) // upstream 에 데이터를 한번에 요청하는 갯수 (1개의 데이터씩만 요청)
                .thenConsumeWhile(num -> num >= 1) // emit 된 데이터들을 소비하면서 검증한다. (1보다 같거나 큰 데이터만 취급)
                .verifyComplete();
        /**
         * expectation "expectComplete" failed (expected: onComplete(); actual: onError(reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals than expected (bounded queue...)))
         */

    }

    @Test
    public void test2() {
        Flux<Integer> flux = Flux.create(emitter -> {
            for (int i = 1; i <= 100; i++) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.ERROR);

        StepVerifier.create(flux, 1)
                .thenConsumeWhile(num -> num >= 1)
                .expectError() // 에러가 발생할 것을 예상한다.
                // 1은 정상요청
                // backpressure 에러 전략은 요청갯수인 1보다 많은 것들이 emit 되어지면 exception 을 발생시킨다.
                // 2에서 exception 이 발생하면 그뒤에 3부터는 다 discard 가 된다.
                .verifyThenAssertThat()
                // 에러가 발생한 이후에 추가적인 동작을 검증이 가능하다.
                .hasDiscardedElements()
                // discard 된 요소가 있는지 검증한다.
                .hasDiscarded(2)
                // 폐기가 된 데이터가 뭔지 구체적으로 검증한다.
                .hasDroppedElements()
                // drop 된 데이터가 존재하는지 확인한다.
                .hasDropped(3, 4, 5, 6, 7, 8, 9, 97, 99, 100);
        // 구체적으로 어떤 데이터가 drop 되었는지 검증한다.
    }


    @Test
    public void test3() {
        Flux<Integer> flux = Flux.create(emitter -> {
            for (int i = 1; i <= 100; i++) {
                emitter.next(i);
            }
            emitter.complete();
        }, FluxSink.OverflowStrategy.DROP);

        /**
         * backpressure drop 전략을 쓰면?
         * 1. error 전략을 쓸때는 에러가 실제로 발생하니  .expectError() 를 사용했다.
         * 하지만 drop 전략은 에러가 발생하는 것이 아니라 데이터를 drop 시켜 정상적으로 시퀀스를 진행시킨다.
         * 그래서 expectComplete() 를 사용한다.
         *
         * 2.hasDiscardedElements(), .hasDiscarded(2,3,4,5,6,7,8,9,10);
         * drop 전략은 실제 drop 시키면서 결국에는 폐기시키기 때문에 discard 로 검증이 된다.
         *
         * !!!주의할점
         * has dropped 는 검증이 안된다.
         * 왜냐? ->
         * Assert that the tested publisher has dropped at least all of the provided
         * elements to the Hooks.onNextDropped(Consumer) hook, in any order.
         *
         * has dropped 메소드 인터페이스에 주석을 보게되면 위와 같은 주석이 있다.
         * Hooks.onNextDropped(Consumer) 훅이 개입해서 onNext 를 drop 하는것이기 때문에 실제
         * 리액터 스펙에서 drop 되는 것과 데이터가 drop 되는 것을 헷갈려서는 안된다.
         */

        StepVerifier.create(flux, 1)
                .thenConsumeWhile(num -> num >= 1)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDiscardedElements()
                .hasDiscarded(2, 3, 4, 5, 6, 7, 8, 9, 10);
//                .hasDropped(2,3,4,5,6,7,8,9,10);


    }

    @Test
    public void test4() {
        Flux<String> flux =
                Flux.just("franch", "russia", "greece", "poland")
                        .map(country -> country.substring(0, 1).toUpperCase() + country.substring(1));

        /**
         * emit 되는 모든 데이터들을 캡쳐하여 컬렉션에 기록한후 기록된 데이터를 검증한다.
         */
        StepVerifier.create(flux)
                .expectSubscription()
                .recordWith(ArrayList::new) // emit 된 데이터를 기록하는 세션을 시작
                .thenConsumeWhile(country -> !country.isEmpty()) // emit 된 데이터가 특정 조건에 부합하면 컬렉션에 기록한다.
                .consumeRecordedWith(countries -> { // 기록된 데이터를 소비하여 데이터를 검증한다.
                    assertThat(countries, everyItem(hasLength(6)));
                    assertThat(countries.stream().allMatch(country -> Character.isUpperCase(country.charAt(0))), Matchers.is(true));
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void test5() {
        Flux<String> flux =
                Flux.just("franch", "russia", "greece", "poland")
                        .map(country -> country.substring(0, 1).toUpperCase() + country.substring(1));

        /**
         * emit 되는 모든 데이터들을 캡쳐하여 컬렉션에 기록한후 기록된 데이터를 검증한다.
         */
        StepVerifier.create(flux)
                .expectSubscription()
                .recordWith(ArrayList::new) // emit 된 데이터를 기록하는 세션을 시작
                .thenConsumeWhile(country -> !country.isEmpty()) // emit 된 데이터가 특정 조건에 부합하면 컬렉션에 기록한다.
                .expectRecordedMatches(countries -> { // consumeRecordedWith 는 AssetThat 을 이용하는 반면에 predicate 를 이용한다.
                    return countries.stream().allMatch(country -> Character.isUpperCase(country.charAt(0)));
                })
                .expectComplete()
                .verify();
    }
}
