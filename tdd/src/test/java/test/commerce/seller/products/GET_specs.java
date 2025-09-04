package test.commerce.seller.products;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import commerce.command.RegisterProductCommand;
import commerce.result.ArrayCarrier;
import commerce.view.SellerMeView;
import commerce.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.TestFixture;

import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.ProductAssertions.isDerivedFrom;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;

@CommerceApiTest
@DisplayName("GET /seller/products")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestFixture testFixture
    ) {

        testFixture.createSellerThenSetAsDefaultUser();

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자가_등록한_모든_상품을_반환한다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();
        List<UUID> ids = testFixture.registerProducts();

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.items()).extracting(SellerProductView::id).containsAll(ids);
    }

    @Test
    void 다른_판매자가_등록한_상품이_포함되지_않는다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();
        UUID unExpected = testFixture.registerProduct();

        testFixture.createSellerThenSetAsDefaultUser();
        testFixture.registerProducts();

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual.items()).extracting(SellerProductView::id).doesNotContain(unExpected);
    }

    @Test
    void 상품_정보를_올바르게_반환한다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand registerProductCommand = generateRegisterProductCommand();
        testFixture.registerProduct(registerProductCommand);

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView item = body.items()[0];
        assertThat(item).satisfies(isDerivedFrom(registerProductCommand));
    }

    @Test
    void 상품_등록_시각을_올바르게_반환한다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();
        LocalDateTime ref = LocalDateTime.now(UTC);
        testFixture.registerProduct();

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        ArrayCarrier<SellerProductView> body = response.getBody();
        SellerProductView item = Objects.requireNonNull(body).items()[0];
        assertThat(item.registeredTimeUtc()).isCloseTo(ref, within(1, ChronoUnit.SECONDS));
    }
    
    @Test
    void 상품_목록을_등록_시점_역순으로_정렬한다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();
        testFixture.registerProducts();

        ResponseEntity<ArrayCarrier<SellerProductView>> response = testFixture.client().exchange(
                RequestEntity.get("/seller/products").build(),
                new ParameterizedTypeReference<>() {
                }
        );

        ArrayCarrier<SellerProductView> body = response.getBody();

        SellerProductView[] items = Objects.requireNonNull(body).items();
        // 상품 등록 시간의 역순으로 정렬되어있는지 확인한다.
        assertThat(items).extracting(SellerProductView::registeredTimeUtc).isSortedAccordingTo(reverseOrder());
    }

}
