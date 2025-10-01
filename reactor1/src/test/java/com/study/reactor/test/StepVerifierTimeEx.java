package com.study.reactor.test;

import com.study.reactor.util.Logger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;

public class StepVerifierTimeEx {

    /**
     *  ############# 시간을 조작하는 방법 1 #############
     */
    @Test
    public void test1() throws InterruptedException {
//        Flux.interval(Duration.ofSeconds(4)).subscribe(data -> Logger.log(String.valueOf(data)));

        Flux<Tuple2<String, Integer>> sequence =
                Flux.interval(Duration.ofHours(12)).take(1)
                        .flatMap(notUse -> {
                            return Flux.just(
                                    Tuples.of("서울", 1000),
                                    Tuples.of("경기도", 1000),
                                    Tuples.of("강원도", 1000),
                                    Tuples.of("충청도", 1000),
                                    Tuples.of("경상도", 1000),
                                    Tuples.of("전라도", 1000),
                                    Tuples.of("인천", 1000),
                                    Tuples.of("대전", 1000),
                                    Tuples.of("부산", 1000),
                                    Tuples.of("대구", 1000),
                                    Tuples.of("제주도", 1000)
                            );
        });

        // sequence 는 12시간 후에 11개의 튜플을 시퀀스로 갖는 flux 가 튀어나온다.
        // 하지만 테스트를 위해서 12시간씩 기다릴 필요가 없다.
        // create 대신 withVirtualTime 를 써주면서 시퀀스를 선언하고
        //.then(() -> VirtualTimeScheduler.get().advanceTimeBy(Duration.ofHours(12))) 를 적어주면 시간을 12시간 앞으로 땡길 수 있다.
        // 그리고 expectxxxx 로 나온 flux 가 11개의 튜플을 가지는지 체크한다.
        StepVerifier
                .withVirtualTime(() -> sequence)
                .expectSubscription()
                .then(() -> VirtualTimeScheduler.get().advanceTimeBy(Duration.ofHours(12)))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    /**
     *  ############# 시간을 조작하는 방법 2 #############
     */
    @Test
    public void test2() {
        Flux<Tuple2<String, Integer>> sequence =
                Flux.interval(Duration.ofHours(12)).take(1)
                        .flatMap(notUse -> {
                            return Flux.just(
                                    Tuples.of("서울", 1000),
                                    Tuples.of("경기도", 1000),
                                    Tuples.of("강원도", 1000),
                                    Tuples.of("충청도", 1000),
                                    Tuples.of("경상도", 1000),
                                    Tuples.of("전라도", 1000),
                                    Tuples.of("인천", 1000),
                                    Tuples.of("대전", 1000),
                                    Tuples.of("부산", 1000),
                                    Tuples.of("대구", 1000),
                                    Tuples.of("제주도", 1000)
                            );
                        });

        // sequence 는 12시간 후에 11개의 튜플을 시퀀스로 갖는 flux 가 튀어나온다.
        // 하지만 테스트를 위해서 12시간씩 기다릴 필요가 없다.
        // create 대신 withVirtualTime 를 써주면서 시퀀스를 선언하고
        // thenAwait 이 VirtualTimeScheduler.get().advanceTimeBy(Duration.ofHours(12))) 와 다른점은
        /**
         * thenAwait : 내 입장(테스트입장)에서 기다리는데 12시간이 빠르게 다가온다.
         * VirtualTimeScheduler : 내 입장(테스트입장)에서 12 시간을 땡겨서 flux 타임에 맞춘다.
         * 실행 결과는 같지만 주체가 다르다.
         */
        // 그리고 expectxxxx 로 나온 flux 가 11개의 튜플을 가지는지 체크한다.
        StepVerifier
                .withVirtualTime(() -> sequence)
                .expectSubscription()
                .thenAwait(Duration.ofHours(12))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    /**
     *  ############# 시간 내에 잘 끝내는지 테스트 하는 방법 #############
     */
    @Test
    public void test3() {
        Flux<Tuple2<String, Integer>> sequence =
                Flux.interval(Duration.ofHours(12)).take(1)
                        .flatMap(notUse -> {
                            return Flux.just(
                                    Tuples.of("서울", 1000),
                                    Tuples.of("경기도", 1000),
                                    Tuples.of("강원도", 1000),
                                    Tuples.of("충청도", 1000),
                                    Tuples.of("경상도", 1000),
                                    Tuples.of("전라도", 1000),
                                    Tuples.of("인천", 1000),
                                    Tuples.of("대전", 1000),
                                    Tuples.of("부산", 1000),
                                    Tuples.of("대구", 1000),
                                    Tuples.of("제주도", 1000)
                            );
                        });

        StepVerifier
                .withVirtualTime(() -> sequence)
                .expectSubscription()
                .expectNextCount(11)
                .expectComplete()
                .verify(Duration.ofSeconds(3)); // 이 테스트가 3초안에 끝나지 않으면 실패!!
    }

    /**
     *  ############# 시간 내에 잘 끝내는지 테스트 하는 방법 2 #############
     */
    @Test
    public void test4() {
        Flux<Tuple2<String, Integer>> sequence = Flux.interval(Duration.ofMinutes(1)).zipWith(Flux.just(
                Tuples.of("중구", 1000),
                Tuples.of("서초구", 1000),
                Tuples.of("강서구", 1000),
                Tuples.of("강동구", 1000),
                Tuples.of("서대문구", 1000)
        )).map(Tuple2::getT2);

        /**
         * StepVerifier.withVirtualTime(() -> sequence)
         *     .expectSubscription()
         *     .expectNoEvent(Duration.ofMinutes(1)) // 1분 동안 없음
         *     .expectNext(Tuples.of("중구", 1000))  // 1분 뒤 첫 값
         *     .expectNoEvent(Duration.ofMinutes(1))
         *     .expectNext(Tuples.of("서초구", 1000)) // 2분 뒤
         *     .expectNoEvent(Duration.ofMinutes(1))
         *     .expectNext(Tuples.of("강서구", 1000)) // 3분 뒤
         *     .expectNoEvent(Duration.ofMinutes(1))
         *     .expectNext(Tuples.of("강동구", 1000)) // 4분 뒤
         *     .expectNoEvent(Duration.ofMinutes(1))
         *     .expectNext(Tuples.of("서대문구", 1000)) // 5분 뒤
         *     .expectComplete()
         *     .verify();
         *
         *     와 아래는 같은 코드이다.
         *     그런데 왜 원래 코드에서는 .expectNextCount(4) 로 끝냈을까?
         *
         * 원래 코드는 일일이 "서초구", "강서구" … 를 다 쓰지 않고,
         * 그냥 "중구" 하나만 명시하고, 나머지는 "그 뒤 4개가 순서대로 나오면 된다" 라는 식으로 간단히 테스트를 작성한 거예요:
         *
         * .expectNext(Tuples.of("중구", 1000)) // 첫 번째 값만 검증
         * ...
         * .expectNextCount(4) // 나머지 4개는 그냥 개수만 확인
         *
         *
         * 즉, 각 값까지 정확히 검증하지는 않고, 개수만 세는 방식이에요.
         *
         * ✅ 정리
         *
         * 네 말대로 정확하게 하려면 expectNext("서초구", …), expectNext("강서구", …) 등을 1분마다 넣는 게 맞습니다.
         *
         * 다만 코드 작성자가 귀찮아서, 첫 값만 검증 → 나머지는 “4개 더 나오면 됨”으로 퉁친 거예요 😅
         */
        StepVerifier.withVirtualTime(() -> sequence)
                .expectSubscription()
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNext(Tuples.of("중구", 1000))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }
}
