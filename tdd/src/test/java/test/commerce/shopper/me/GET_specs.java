package test.commerce.shopper.me;

import commerce.view.ShopperMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.TestFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("GET /shopper/me")
public class GET_specs {
    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ){
        // Given
        String token = fixture.createShopperThenIssueToken();

        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .header("Authorization", "Bearer " + token)
                .build(), ShopperMeView.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
    
    @Test
    void 접근_토큰을_사용하지_않으면_401_Unautuorized_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .build(), ShopperMeView.class);

        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 서로_다른_구매자의_식별자는_서로_다르다(
        @Autowired TestFixture fixture
    ) {
        String token1 = fixture.createShopperThenIssueToken();
        String token2 = fixture.createShopperThenIssueToken();

        ResponseEntity<ShopperMeView> response1 = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .header("Authorization", "Bearer " + token1)
                .build(), ShopperMeView.class);

        ResponseEntity<ShopperMeView> response2 = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .header("Authorization", "Bearer " + token2)
                .build(), ShopperMeView.class);

        assertThat(response1.getBody().id()).isNotEqualTo(response2.getBody().id());

    }

    @Test
    void 같은_구매자의_식별자는_항상_같다(
        @Autowired TestFixture fixture
    ) {
        String token1 = fixture.createShopperThenIssueToken();

        ResponseEntity<ShopperMeView> response1 = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .header("Authorization", "Bearer " + token1)
                .build(), ShopperMeView.class);

        ResponseEntity<ShopperMeView> response2 = fixture.client().exchange(
            RequestEntity.get("/shopper/me")
                .header("Authorization", "Bearer " + token1)
                .build(), ShopperMeView.class);

        assertThat(response1.getBody().id()).isEqualTo(response2.getBody().id());

    }
    
    @Test
    void 구매자의_기본_정보가_올바르게_설정된다(@Autowired TestFixture fixture) {

        String email = generateEmail();
        String password = generatePassword();
        String username = generateUsername();

        fixture.createShopper(email, username, password);

        fixture.setShopperAsDefaultUser(email, password);
        ShopperMeView actual = fixture.client().getForObject("/shopper/me", ShopperMeView.class);

        assertThat(actual.email()).isEqualTo(email);
        assertThat(actual.username()).isEqualTo(username);
    }

}
