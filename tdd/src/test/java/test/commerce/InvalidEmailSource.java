package test.commerce;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.params.provider.MethodSource;

@Retention(RetentionPolicy.RUNTIME)
@MethodSource("test.commerce.TestDataSource#invalidEmails")
public @interface InvalidEmailSource {
}
