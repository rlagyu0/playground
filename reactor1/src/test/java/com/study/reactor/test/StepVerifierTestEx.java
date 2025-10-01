package com.study.reactor.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class StepVerifierTestEx {

    @Test
    public void test1() {
        StepVerifier
                .create(Mono.just("Hello World")) // 검증할 시퀀스를 파라미터로
                .expectNext("Hello World") // 시퀀스가 emit 하는 데이터가 일치하는지 검증
                .expectComplete() // onComplete 시그널이 발생하여 종료되는지 검증
                .verify(); // 이벤트 발생 시키는 트리거 같은 역할
    }

    @Test
    public void test2() {
        StepVerifier
                .create(Flux.just("Hello", "World"))
                .expectSubscription() // 구독이 정상적으로 실행 됐는지 (시퀀스에서 데이터가 emit 이 된다면 정상적으로 구독이 되었다고 봐도 무방)
                .expectNext("Hello")    // 시퀀스의 첫번째 데이터 검증
                .expectNext("World") // 시퀀스의 두번째 데이터 검증
                .expectComplete()
                .verify();
    }

    @Test
    public void test3() {
        StepVerifier
                .create(Flux.just("Hello", "World"))
                // 테스트 시작
                .expectSubscription()
                /**
                 * as 는 as 바로 위의 expectxxxx() 메소드가 실패할 경우에 해당 expectxxxx에 이름을 정해서
                 * 로깅을 띄워준다. (예를들어 구독에 실패하면 expectSubscription() 테스트 구간의 이름이 #exepect subscribe 로 지정되고
                 * 로깅에 해당 이름과 함께 에러를 발생
                 * 그리고 다음 테스트 체인은 실행되지 않는다.
                 */
                .as("# expect subscribe")
                /**
                 * 에러를 발생시킬 구간이다.
                 * Hello 를 정답으로 해야하지만 Hi 를 expect 하고 있음으로 아래와 같은 에러가 발생한다.
                 * ======================== 에러 내용 ========================
                 * expectation "# expect Hi" failed (expected value: Hi; actual value: Hello)
                 */
                .expectNext("Hi")
                .as("# expect Hi")
                .expectNext("World")
                .as("# expect World")
                .verifyComplete();              // verify complete 메소드는 테스트 실행의 트리거와 동시에 complete 시그널을 반환하는지 동시에 체크
    }

    @Test
    public void test4() {
        StepVerifier
                .create(
                    Flux.just(2,4,6,8,10).zipWith(Flux.just(2,2,2,2,0), (x, y) -> x / y)
                ) // (2/2) (4/2) (6/2) (8/2) (10/0 * error) 즉, 1,2,3,4,error 발생!!
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4) // 여기까지 순서대로 기댓값을 충족하지만
                .expectError() // 10/0 에서 에러를 발생시킨다.
                .verify();
    }

    @Test
    public void test5() {
        StepVerifier
                .create(
                        Flux.just(2,4,6,8,10).zipWith(Flux.just(2,2,2,2,2), (x, y) -> x / y)
                ) // (2/2) (4/2) (6/2) (8/2) (10/2) 즉, 1,2,3,4,5
                .expectSubscription()
                /**
                 * .expectNext(1)
                 * .expectNext(2)
                 * .expectNext(3)
                 * .expectNext(4)
                 * .expectNext(5)
                 * 이렇게 늘려서 쓰면 불편하기도 하니 아래처럼
                 * .expectNext(1,2,3,4,5) 이렇게 해줘도 검증이 똑같이 가능하다.
                 */
                .expectNext(1,2,3,4,5)
                .expectComplete()
                .verify();
    }

    @Test
    public void test6() {
        // 0~499 방출 (총 500개)
        Flux<Integer> flux = Flux.range(0, 1000).take(500);

        StepVerifier
                .create(flux, StepVerifierOptions.create().scenarioName("scenario"))
                .expectSubscription()
                .expectNext(0) // 처음 데이터가 emit 되면 0이 emit 될거임 (1개가  emit 됨)
                .expectNextCount(498) // 0 이 방출되고나면 다음으로부터 498개가 방출됨 ( 0포함 499 개 방출)
                .expectNext(499) // 마지막 500번째 데이터인 499 검증 (500 개 방출)
                .expectComplete()
                .verify();

        /**
         * .expectNextCount(498) 이게 없으면
         *
         * .expectNext(0)
         * .expectNext(1)
         * .expectNext(2)
         * ... 이런식으로
         * .expectNext(499) 까지 써줘야한다.
         *
         */
    }
}
