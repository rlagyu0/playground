package com.study.reactor.test;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

/**
 * 📌 PublisherProbe를 이용한 Testing
 * 1.Operator 체인 실행 경로 검증 가능
 * 2.조건 분기에 따라 Sequence가 분기되는 경우, 실행 경로를 추적하여 정상 실행 여부 확인 가능
 * 3.실행 경로가 정상적으로 수행되었는지 검증하는 메서드:
 * assertWasSubscribed()
 * assertWasRequested()
 * assertWasCancelled()
 *
 * 조건에 따라 데이터가 존재하면 해당 Flux 반환, 그렇지 않으면 빈 Flux 반환
 * PublisherProbe를 이용해서 어느 경로가 실행됐는지 검증
 */
public class StepVerifierPublishProbeEx {

    @Test
    public void test1() {
        Mono<String> empty = Mono.empty();
        PublisherProbe<String> useMono = PublisherProbe.of(Mono.just("default"));

        // 만약 empty 가 비어있으면 useMono 를 사용한다.
        Mono<String> tryMono = empty
                .flatMap(s -> Mono.just(s))
                .switchIfEmpty(useMono.mono());

        StepVerifier.create(tryMono)
                .expectNext("default")
                .verifyComplete();

        useMono.assertWasNotCancelled();
        useMono.assertWasSubscribed();
        useMono.assertWasRequested();
    }

    @Test
    void sequenceBranchingTest() {
        boolean condition = true; // 조건 분기: true면 데이터 반환, false면 빈 Flux 반환

        PublisherProbe<String> dataProbe = PublisherProbe.of(Flux.just("A", "B", "C"));
        PublisherProbe<String> emptyProbe = PublisherProbe.empty();

        Flux<String> result = condition
                ? dataProbe.flux()
                : emptyProbe.flux();

        // 구독
        result.collectList().block();

        // ✅ 조건이 true라면 dataProbe가 실행되었는지 검증
        dataProbe.assertWasSubscribed();
        dataProbe.assertWasRequested();
        dataProbe.assertWasNotCancelled();

        // ✅ 반대로 emptyProbe는 실행되지 않았는지 확인
        emptyProbe.assertWasNotSubscribed();
    }

    @Test
    void sequenceBranchingTest_FalseCase() {
        boolean condition = false;

        PublisherProbe<String> dataProbe = PublisherProbe.of(Flux.just("X", "Y"));
        PublisherProbe<String> emptyProbe = PublisherProbe.empty();

        Flux<String> result = condition
                ? dataProbe.flux()
                : emptyProbe.flux();

        result.collectList().block();

        // ✅ 조건이 false라면 emptyProbe 실행 검증
        emptyProbe.assertWasSubscribed();
        emptyProbe.assertWasRequested();

        // ✅ dataProbe는 실행되지 않음
        dataProbe.assertWasNotSubscribed();
    }
}
