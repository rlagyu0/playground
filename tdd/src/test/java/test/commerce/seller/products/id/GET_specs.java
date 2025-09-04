package test.commerce.seller.products.id;

import java.time.LocalDateTime;
import java.util.UUID;

import commerce.command.RegisterProductCommand;
import commerce.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.RegisterProductCommandGenerator;
import test.commerce.TestFixture;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.ProductAssertions.isDerivedFrom;

@CommerceApiTest
@DisplayName("GET /seller/products/{id}")
public class GET_specs {
    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestFixture fixture
        ) {

        fixture.createSellerThenSetAsDefaultUser();
        UUID id = fixture.registerProduct();

        ResponseEntity<?> response =
            fixture.client().getForEntity("/seller/products/" + id, SellerProductView.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
    
    @Test
    void 판매자가_아닌_사용자의_접근_토큰을_사용하면_403_Forbidden_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {

        fixture.createSellerThenSetAsDefaultUser();
        UUID id = fixture.registerProduct();

        fixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<?> response
            = fixture.client().getForEntity("/seller/products/" + id, SellerProductView.class);

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }
    
    
    @Test
    void 존재하지_않는_상품_식별자를_사용하면_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        fixture.createSellerThenSetAsDefaultUser();
        UUID id = UUID.randomUUID();

        ResponseEntity<?> response
            = fixture.client().getForEntity("/seller/products/" + id, SellerProductView.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
    
    @Test
    void 다른_판매자가_등록한_상품_식별자를_사용하면_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        fixture.createSellerThenSetAsDefaultUser();
        UUID id = fixture.registerProduct();

        fixture.createSellerThenSetAsDefaultUser();
        ResponseEntity<?> response
            = fixture.client().getForEntity("/seller/products/" + id, SellerProductView.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
    
    @Test
    void 상품_식별자를_올바르게_반환한다(
        @Autowired TestFixture fixture
    ) {
        fixture.createSellerThenSetAsDefaultUser();
        UUID id = fixture.registerProduct();

        SellerProductView response
            = fixture.client().getForObject("/seller/products/" + id, SellerProductView.class);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void 상품_정보를_올바르게_반환한다(
        @Autowired TestFixture fixture
    ) {
        fixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = RegisterProductCommandGenerator.generateRegisterProductCommand();
        UUID id = fixture.registerProduct(command);

        SellerProductView response
            = fixture.client().getForObject("/seller/products/" + id, SellerProductView.class);

        assertThat(response).satisfies(isDerivedFrom(command));
    }
    
    @Test
    void 상품_등록_시각을_올바르게_반환한다(
        @Autowired TestFixture fixture
    ){
        fixture.createSellerThenSetAsDefaultUser();

        LocalDateTime ref = LocalDateTime.now(UTC);

        UUID uuid = fixture.registerProduct();

        SellerProductView response
            = fixture.client().getForObject("/seller/products/" + uuid, SellerProductView.class);

        assertThat(response).isNotNull();
        assertThat(response.registeredTimeUtc()).isCloseTo(ref, within(1,SECONDS));
    }
}
