package commerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
public class CommerceApiApp {

    public static void main(String[] args) throws JsonProcessingException {
//        SpringApplication.run(CommerceApiApp.class, args);

        BuzzvilTest test = BuzzvilTest.builder().
            puid("624877").
            birthday("1998-10-09").
            year_of_birth("1998").
            gender("M").
            platform("A").
            carrier("skt").
            device_name("SHV-E250S").
            region(URLEncoder.encode("경기 성남시", StandardCharsets.UTF_8)).build();

        String userInfo = new ObjectMapper().writeValueAsString(test);
        String uriEncoded = URLEncoder.encode(userInfo, StandardCharsets.UTF_8);
        String base64Encoded = Base64.getEncoder()
            .encodeToString(uriEncoded.getBytes(StandardCharsets.UTF_8));

        System.out.println(base64Encoded);


    }

    @Builder
    public record BuzzvilTest(
        String puid,
        String birthday,
        String year_of_birth,
        String gender,
        String platform,
        String carrier,
        String device_name,
        String region
    ) {
    }
}
