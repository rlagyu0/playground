package test.commerce.seller.me;

import commerce.command.CreateSellerCommand;
import commerce.query.IssueSellerToken;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import commerce.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.EmailGenerator;
import test.commerce.TestFixture;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.get;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("GET /seller/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200OK_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        String email = EmailGenerator.generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        CreateSellerCommand createSellerCommand = new CreateSellerCommand(email, username, password, generateEmail());
        client.postForEntity("/seller/signup", createSellerCommand, Void.class);

        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        String token = carrier.accessToken();

        ResponseEntity<SellerMeView> response = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token)
                        .build(),
                SellerMeView.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_UNAUTHORIZED_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        ResponseEntity<Void> response = client.getForEntity(
                "/seller/me",
                Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 서로_다른_판매자의_식별자는_서로_다르다(@Autowired TestRestTemplate client) {

        String email1 = EmailGenerator.generateEmail();
        String username1 = generateUsername();
        String password1 = generatePassword();

        CreateSellerCommand createSellerCommand1 = new CreateSellerCommand(email1, username1, password1, generateEmail());
        client.postForEntity("/seller/signup", createSellerCommand1, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email1, password1),
                AccessTokenCarrier.class
        );

        String token1 = carrier1.accessToken();

        String email2 = EmailGenerator.generateEmail();
        String username2 = generateUsername();
        String password2 = generatePassword();

        CreateSellerCommand createSellerCommand2 = new CreateSellerCommand(email2, username2, password2, generateEmail());
        client.postForEntity("/seller/signup", createSellerCommand2, Void.class);

        AccessTokenCarrier carrier2 = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email2, password2),
                AccessTokenCarrier.class
        );

        String token2 = carrier2.accessToken();

        ResponseEntity<SellerMeView> response1 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token1)
                        .build(),
                SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token2)
                        .build(),
                SellerMeView.class
        );

        assertThat(response1.getBody().id()).isNotEqualTo(response2.getBody().id());
    }

    @Test
    void 같은_판매자의_식별자는_항상_같다(
            @Autowired TestRestTemplate client
    ) {
        String email1 = EmailGenerator.generateEmail();
        String username1 = generateUsername();
        String password1 = generatePassword();

        CreateSellerCommand createSellerCommand1 = new CreateSellerCommand(email1, username1, password1, generateEmail());
        client.postForEntity("/seller/signup", createSellerCommand1, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email1, password1),
                AccessTokenCarrier.class
        );
        String token1 = carrier1.accessToken();

        AccessTokenCarrier carrier2 = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email1, password1),
                AccessTokenCarrier.class
        );

        String token2 = carrier2.accessToken();

        ResponseEntity<SellerMeView> response1 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token1)
                        .build(),
                SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token2)
                        .build(),
                SellerMeView.class
        );

        assertThat(response1.getBody().id()).isEqualTo(response2.getBody().id());
    }

    @Test
    void 판매자의_기본_정보가_올바르게_설정된다(@Autowired TestRestTemplate client) {

        String email1 = EmailGenerator.generateEmail();
        String username1 = generateUsername();
        String password1 = generatePassword();

        CreateSellerCommand createSellerCommand1 = new CreateSellerCommand(email1, username1, password1, generateEmail());
        client.postForEntity("/seller/signup", createSellerCommand1, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject(
                "/seller/issuetoken",
                new IssueSellerToken(email1, password1),
                AccessTokenCarrier.class
        );
        String token1 = carrier1.accessToken();

        ResponseEntity<SellerMeView> response1 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token1)
                        .build(),
                SellerMeView.class
        );

        assertThat(response1.getBody().username()).isEqualTo(username1);
        assertThat(response1.getBody().email()).isEqualTo(email1);

    }

    @Test
    void 문의_이메일_주소를_올바르게_설정한다(
            @Autowired TestFixture testFixture
    ) {
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();
        String contactEmail = generateEmail();

        testFixture.createSeller(email,
                username,
                password,
                contactEmail);

        AccessTokenCarrier carrier = testFixture.client()
                .postForObject(
                        "/seller/issuetoken",
                        new IssueShopperToken(email, password),
                        AccessTokenCarrier.class
                );

        String token = carrier.accessToken();

        ResponseEntity<SellerMeView> response = testFixture.client().
                exchange(get("/seller/me").
                        header("Authorization",
                                "Bearer " + token)
                        .build(), SellerMeView.class);

        SellerMeView actual = response.getBody();
        assertThat(Objects.requireNonNull(actual).contactEmail()).isEqualTo(contactEmail);


    }
}
