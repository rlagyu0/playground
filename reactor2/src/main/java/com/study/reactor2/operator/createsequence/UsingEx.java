package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.LogType;
import com.study.reactor2.util.Logger;
import reactor.core.publisher.Mono;

public class UsingEx {
    public static void main(String[] args) {
        /**
         * using 의 파라미터
         * 1. Callable : 제공해야할 리소스를 제공한다.
         * 2. 1에서 제공받은 리소스를 변형하거나, 새로 생성한 Publisher 로 emit 한다.
         * 3. emit 후에 구독을 하고 사용이 끝난 데이터의 자원을 해제하는 작업을 한다.
         *      -  onComplete 나 onError 후에 resource 를 해제할 수 있다.
         *
         *  언제쓰냐??
         *  파일 데이터를 스트림 형태로 불러오고 사용한 후에 clean up 작업때 close 해줄 수 있음. (자원해제)
         */
        Mono.using(
                () -> "Resource",
                resource -> {
                    Logger.log("Resource chage start!!! : " + resource);
                    return Mono.just("changed -> " + resource);
                },
                resource -> {
                    Logger.log("clean up resource" + resource);
                }
        ).subscribe(resource -> Logger.log(LogType.ON_NEXT, resource));

        /**
         * Resource chage start!!! : Resource | main | 21:19:14.655
         * - onComplete 후
         * clean up resourceResource | main | 21:19:14.661
         *
         * 실제 구독!!!
         * onNext() | main | 21:19:14.663 | changed -> Resource
         */
    }
}
