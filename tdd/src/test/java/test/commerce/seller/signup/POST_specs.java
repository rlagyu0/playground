package test.commerce.seller.signup;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.commerce.CommerceApiTest;
import test.commerce.TestFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /seller/signup")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(generateEmail(), generateUsername(), "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(null, generateUsername(), "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("test.commerce.TestDataSource#invalidEmails")
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            String email,
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(email, generateUsername(), "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(generateEmail(), null, "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "se",
            "김규영!",
            "se@",
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
            String username,
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(generateEmail(), username, "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "김규영",
            "username",
            "us111e112rnam22",
            "dvnjsoe123"
    })
    void username_속성이_올바른_형식을_따르면_204_No_Content_상태코드를_반환한다(
            String username,
            @Autowired TestRestTemplate client
    ) {
        //Arrange
        var command = new CreateSellerCommand(generateEmail(), username, "password", generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //Arrange
        var command = new CreateSellerCommand(generateEmail(), generateUsername(), null, generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "pass",
            "passwd1",
            "  "
    })
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(String password, @Autowired TestRestTemplate client) {
        //Arrange
        var command = new CreateSellerCommand("seller@test.com", generateUsername(), password, generateEmail());
        //Act
        ResponseEntity<Void> response = client.postForEntity("/seller/signup", command, Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void email_속성에_이미_존재하는_이메일_주소가_지정되면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //Arrange
        String email = generateEmail();
        client.postForEntity("/seller/signup",
                new CreateSellerCommand(email, generateUsername(), "password", generateEmail()),
                Void.class);
        //Act
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(email, generateUsername(), "password", generateEmail()),
                Void.class
        );

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성에_이미_존재하는_사용자이름이_지정되면_400_Bad_Request_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        //Arrange
        String username = generateUsername();
        client.postForEntity("/seller/signup",
                new CreateSellerCommand(generateEmail(), username, "password", generateEmail()),
                Void.class);
        //Act
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signup",
                new CreateSellerCommand(generateEmail(), username, "password", generateEmail()),
                Void.class
        );

        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(
            @Autowired TestRestTemplate client,
            @Autowired PasswordEncoder encoder,
            @Autowired SellerRepository repository
    ) {
        //Arrange
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                generatePassword(), generateEmail());

        //Act
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signup",
                command,
                Void.class
        );

        //Assert
        Seller seller = repository
                .findAll()
                .stream()
                .filter(x -> x.getEmail().equals(command.email()))
                .findFirst()
                .orElseThrow();

        String actual = seller.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(encoder.matches(command.password(), actual)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("test.commerce.TestDataSource#invalidEmails")
    void contactEamil_속성이_올바르게_지정되지_않으면_400_BadRequest_상태코드_반환(
            String contactEmail,
            @Autowired TestFixture testFixture
    ) {
        //Arrange
        var command = new CreateSellerCommand(
                generateEmail(),
                generateUsername(),
                "password",
                contactEmail);
        //Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
                "/seller/signup",
                command,
                Void.class);
        //Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);


    }
}
