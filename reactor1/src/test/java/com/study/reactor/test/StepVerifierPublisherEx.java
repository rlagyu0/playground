package com.study.reactor.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

/**
 * 1. Testing 목적에 사용하기위한 Publisher 를 테스트한다.
 * 2. 개발자가 직접 프로그래밍을 통해 signal 을 발생시킬 수 있다.
 * 3. 주로 특정한 상황을 재현하여 테스트하고싶은 경우 사용할 수 있다.
 * 4. 리액티브스트림즈의 사양을 준수하는지의 여부를 테스트할 수 있다.
 * - 만약 null 값을 emit 할 수 는 없다.
 * - 만약 구독 취소가 됐는데 emit 하면 리액티브 스트림즈의 스펙을 준수하지 않느 것 등등
 */
public class StepVerifierPublisherEx {

    @Test
    public void test1() {
        // 테스트 목적에 맞게 플럭스를 만들고, 직접 테스트코드에서 데이터를 emit 시킴
        TestPublisher<Integer> testPublisher = TestPublisher.create();

        StepVerifier.create(testPublisher.flux().zipWith(Flux.just(2,2,2,2,2), (a,b) -> a / b))
                .expectSubscription()
                .then(() -> testPublisher.next(2,4,6,8,10))
                .expectNext(1,2,3,4,5)
                .expectComplete()
                .verify();

    }

    @Test
    public void test2() {
        // 테스트 목적에 맞게 플럭스를 만들고, 직접 테스트코드에서 데이터를 emit 시킴
        TestPublisher<Integer> testPublisher = TestPublisher.create();

        StepVerifier.create(testPublisher.flux().zipWith(Flux.just(2,2,2,2,0), (a,b) -> a / b))
                .expectSubscription()
                .then(() -> testPublisher.next(2,4,6,8,10)) // 10/0 을 하게되면 ArithmeticException 이 발생하는데 아래와 같이 exception 을 직접 내주기도 할 수 있다.
//                .then(() -> {
//                    testPublisher.next(2,4,6,8); // zipwith 의 5개 데이터와 달리 4개만 방출하고 에러를 코드로 방출할 수도있다.
//                    testPublisher.error(new ArithmeticException());
//                })
                .expectNext(1,2,3,4)
                .expectError()
                .verify();

    }

    /**
     * next 와 emit 의 차이
     */
    @Test
    public void test3() {
        // 테스트 목적에 맞게 플럭스를 만들고, 직접 테스트코드에서 데이터를 emit 시킴
        TestPublisher<Integer> testPublisher = TestPublisher.create();

        StepVerifier.create(testPublisher.flux().take(3))
                .expectSubscription()
                .then(() -> testPublisher.emit(1,2,3,4,5))
                /**
                 * testPublisher.emit(1,2,3,4,5) 과 testPublisher.next(1,2,3,4,5) 의 차이
                 * testPublisher.emit 메소드 안에는 자체적으로 next() 를 호출하며 마지막에 onComplete() 까지 호출한다.
                 *
                 * 반면에
                 *
                 * testPublisher.next() 는 next() 만 콜하기 때문에
                 * onComplete 가 호출되지 않아 테스트가 끝나지 않고 무한대기한다.
                 */
                .expectNext(1,2,3)
                .expectComplete()
                .verify();
    }

    /**
     * 리액티브 스트림즈의 사양을 준수하는지?
     */
    @Test
    public void test4() {
        // 원래 기본적으로 리액티브 스트림즈는 NULL 방출을 금지한다.
        // 하지만 ALLOW_NULL 옵션을 통해서 스트림즈 사양을 위반하는 퍼블리셔를 생성할 수 있다.
        TestPublisher<Integer> noncompliant = TestPublisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL);
        TestPublisher<Integer> publisher = TestPublisher.create();

        // null 방출이 되는 퍼블리셔 적용시
        // a / b 에서 2 / null 이니, null point exception 발생
        StepVerifier.create(noncompliant.flux().zipWith(Flux.just(2,2,2,2,2), (a,b) -> a / b))
                .expectSubscription()
                .then(() -> publisher.next(2,4,6,8,null))
                .expectNext(1,2,3,4)
                .expectComplete()
                .verify();

        // 스펙을 준수하는 퍼블리셔 적용시
        // null 방출 자체를 보는 에러가 발생한다.
        StepVerifier.create(noncompliant.flux().zipWith(Flux.just(2,2,2,2,2), (a,b) -> a / b))
                .expectSubscription()
                .then(() -> publisher.next(2,4,6,8,10))
                .expectNext(1,2,3,4,5)
                .expectComplete()
                .verify();


    }
}
