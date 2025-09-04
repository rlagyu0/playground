package test.commerce.shopper.products;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import commerce.command.RegisterProductCommand;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import commerce.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.commerce.CommerceApiTest;
import test.commerce.TestFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.ProductAssertions.isViewDerivedFrom;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;

@CommerceApiTest
@DisplayName("GET /shopper/products")
public class GET_specs {

    private static final int PAGE_SIZE = 10;

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(@Autowired TestFixture testFixture) {
        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자_접근_토큰을_사용하면_403_Forbidden_상태코드를_반환한다(@Autowired TestFixture testFixture) {
        testFixture.createSellerThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }
    
    @Test
    void 첫_번째_페이지의_상품을_반환한다(@Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();
        testFixture.createSellerThenSetAsDefaultUser();
        List<UUID> ids = testFixture.registerProducts(PAGE_SIZE);
        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        PageCarrier<ProductView> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.items()).extracting(ProductView::id).containsAll(ids);
    }
    
    @Test
    void 상품_목록을_등록_시점_역순으로_정렬한다(@Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();

        testFixture.createSellerThenSetAsDefaultUser();
        UUID uuid1 = testFixture.registerProduct();
        UUID uuid2 = testFixture.registerProduct();
        UUID uuid3 = testFixture.registerProduct();

        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        ProductView[] items = Objects.requireNonNull(response.getBody()).items();
        System.out.println("items = " + items);
        assertThat(items)
            .extracting(ProductView::id)
            .containsExactly(uuid3, uuid2, uuid1);
    }
    
    @Test
    void 상품_정보를_올바르게_반환한다(@Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();

        testFixture.createSellerThenSetAsDefaultUser();
        RegisterProductCommand command = generateRegisterProductCommand();
        testFixture.registerProduct(command);

        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        ProductView item = response.getBody().items()[0];
        assertThat(item).satisfies(isViewDerivedFrom(command));
    }
    
    @Test
    void 판매자_정보를_올바르게_반환한다(@Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();

        testFixture.createSellerThenSetAsDefaultUser();

        SellerMeView seller = testFixture.getSeller();

        RegisterProductCommand command = generateRegisterProductCommand();

        testFixture.registerProduct(command);

        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        ProductView item = response.getBody().items()[0];
        assertThat(item.seller()).isNotNull();
        assertThat(item.seller().id()).isEqualTo(seller.id());
        assertThat(item.seller().username()).isEqualTo(seller.username());
        
    }
    
    @Test
    void 두_번째_페이지를_올바르게_반환한다(@Autowired TestFixture testFixture){
        testFixture.deleteAllProducts();

        testFixture.createSellerThenSetAsDefaultUser();
        // 5 개 생성
        testFixture.registerProducts(PAGE_SIZE / 2);

        // 10 개 생성
        List<UUID> ids = testFixture.registerProducts(PAGE_SIZE);

        // 10 개 생성
        testFixture.registerProducts(PAGE_SIZE);

        testFixture.createShopperThenSetAsDefaultUser();

        String token = testFixture.consumeProductPage();


        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products?continuationToken=" + token).build(),
            new ParameterizedTypeReference<>() { }
        );

        assertThat(Objects.requireNonNull(response.getBody()).items())
            .extracting(ProductView::id)
            .containsExactlyElementsOf(ids.reversed());

    }

    @ParameterizedTest
    @ValueSource(ints = { 1 , PAGE_SIZE })
    void 마지막_페이지를_올바르게_반환한다(
        int lastPageSize,
        @Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();

        testFixture.createSellerThenSetAsDefaultUser();
        // 1 개 생성
        List<UUID> ids = testFixture.registerProducts(lastPageSize);

        // 20 개 생성
        testFixture.registerProducts(PAGE_SIZE * 2);
        testFixture.createShopperThenSetAsDefaultUser();

        // 페이지 두개 소비
        String token = testFixture.consumeTwoProductPage();

        // 마지막 페이지 가져오기 last 1 개
        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products?continuationToken=" + token).build(),
            new ParameterizedTypeReference<>() { }
        );

        PageCarrier<ProductView> actual = response.getBody();
        assertThat(Objects.requireNonNull(actual).items())
            .extracting(ProductView::id)
            .containsExactlyElementsOf(ids.reversed());

        assertThat(actual.continuationToken()).isNull();

    }

    @Test
    void continuationToken이_없으면_첫_페이지를_반환한다(@Autowired TestFixture testFixture) {
        testFixture.deleteAllProducts();
        testFixture.createSellerThenSetAsDefaultUser();

        // 마지막 페이지 20개 (2,3) 페이지
        testFixture.registerProducts(PAGE_SIZE * 2);

        // 첫 페이지 10
        List<UUID> ids = testFixture.registerProducts(PAGE_SIZE);
        testFixture.createShopperThenSetAsDefaultUser();

        // 마지막 페이지 가져오기 last 1 개
        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        PageCarrier<ProductView> actual = response.getBody();
        assertThat(Objects.requireNonNull(actual).items())
            .extracting(ProductView::id)
            .containsExactlyElementsOf(ids.reversed());

    }

    @Test
    void 문의_이메일_주소를_올바르게_설정한다(@Autowired TestFixture testFixture){
        testFixture.deleteAllProducts();
        testFixture.createSellerThenSetAsDefaultUser();

        SellerMeView seller = testFixture.getSeller();
        testFixture.registerProduct();

        testFixture.createShopperThenSetAsDefaultUser();

        ResponseEntity<PageCarrier<ProductView>> response = testFixture.client().exchange(
            RequestEntity.get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        ProductView actual = Objects.requireNonNull(response.getBody()).items()[0];
        assertThat(actual.seller().contactEmail()).isEqualTo(seller.contactEmail());
    }

}
