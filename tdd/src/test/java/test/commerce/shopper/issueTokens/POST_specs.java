package test.commerce.shopper.issueTokens;

import commerce.command.CreateShopperCommand;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.JwtAssertions.conformsToJwtFormat;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /shopper/issue-tokens")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //Arrange
        String email = generateEmail();
        String password = generatePassword();

        var command = new CreateShopperCommand(email, generateUsername(), password);
        client.postForEntity("/shopper/signup", command, Void.class);

        //Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/shopper/issuetoken",
            new IssueShopperToken(email, password)
            , AccessTokenCarrier.class
        );

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(@Autowired TestRestTemplate client) {
        //Arrange
        String email = generateEmail();
        String password = generatePassword();

        var command = new CreateShopperCommand(email, generateUsername(), password);
        client.postForEntity("/shopper/signup", command, Void.class);

        //Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/shopper/issuetoken",
            new IssueShopperToken(email, password)
            , AccessTokenCarrier.class
        );

        //Assert
        String actual = response.getBody().accessToken();
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        //Arrange
        String email = generateEmail();
        String password = generatePassword();

        //Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity("/shopper/issuetoken",
            new IssueShopperToken(email, password)
            , AccessTokenCarrier.class
        );

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

}
