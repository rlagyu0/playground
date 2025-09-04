package test.commerce.seller.changecontactemail;

import commerce.command.ChangeContactEmailCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.InvalidEmailSource;
import test.commerce.TestFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;

@CommerceApiTest
@DisplayName("POST /seller/changeContactEmail")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
            @Autowired TestFixture testFixture
    ) {
        testFixture.createSellerThenSetAsDefaultUser();
        String contentEmail = generateEmail();

        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(contentEmail),
            Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
    
    
    @ParameterizedTest
    //    @MethodSource("test.commerce.TestDataSource#invalidEmails")
    @InvalidEmailSource
    void contactEmail_속성이_올바르게_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
            String email,
            @Autowired TestFixture testFixture
    ) {
        testFixture.createSellerThenSetAsDefaultUser();

        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(email),
            Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        
    }

    @Test
    void 문의_이메일_주소를_올바르게_변경한다(
        @Autowired TestFixture testFixture
    ) {
        testFixture.createSellerThenSetAsDefaultUser();

        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(generateEmail()),
            Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }


}
