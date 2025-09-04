package test.commerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ThrowingConsumer;

import java.io.IOException;
import java.util.Base64;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtAssertions {
    public static ThrowingConsumer<String> conformsToJwtFormat() {
        return s -> {
            String[] parts = s.split("\\.");
            assertThat(parts).hasSize(3);
            assertThat(parts[0]).matches(JwtAssertions::isBase64UrlEncodedJson);
            assertThat(parts[1]).matches(JwtAssertions::isBase64UrlEncodedJson);
            assertThat(parts[2]).matches(JwtAssertions::isBase64UrlEncoded);
        };
    }

    private static boolean isBase64UrlEncodedJson(String s1) {
        try {
            new ObjectMapper().readTree(Base64.getUrlDecoder().decode(s1));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isBase64UrlEncoded(String s1) {
        try {
            Base64.getUrlDecoder().decode(s1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
