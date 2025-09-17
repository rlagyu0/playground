package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class SinkOneExample {
    public static void main(String[] args) {

        Sinks.One<String> one = Sinks.one();
        // - FAIL_FAST
        // 데이터 EMIT 에
        // 실패할 경우 false 를 리턴할 경우 프로세스 종료
        // true 반환을 하면 실패를 했지만 다시 시도 할 수 있다.
        // FAIL_FAST 는 (signalType, emission) -> false; 로 되어있다.
        // 실패에 대한 케이스도 내가 람다로 짜주면 재시도를 할지 프로세스를 종료할지 정하면된다.

        // - void emitValue(@Nullable T value, EmitFailureHandler failureHandler);
        // 내부적으로 같은 inner class 에 정의 되어있는 EmitResult tryEmitValue(@Nullable T value); 를 사용한다.
        // 결국엔 emitValue 는 실패했을 경우의 핸들러라고 보면 된다.
        one.emitValue("Hello Reactor", Sinks.EmitFailureHandler.FAIL_FAST);

        // sinks one 은 하나만 방출하는 역할을 한다. 즉, 모노 생산자로 변경이 가능하다는 것인데,
        // one.emitValue("Hello", Sinks.EmitFailureHandler.FAIL_FAST);
        // 처럼 하나 더 방출하고싶으면 해당 "Hello" 는 정상방출되지 않고 drop 이 된다.

        // 위에 까지가 생산자의 역할이다.
        // 생상자의 emit 해주는 동작과 생산자를 통해 정의 하였으니 구독자도 필요하다.
        // asMono() 로 타입을 바꿔서 subscribe 를 해준다.
        Mono<String> mono = one.asMono();
        mono.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));
        mono.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe2 " + data));

    }
}
