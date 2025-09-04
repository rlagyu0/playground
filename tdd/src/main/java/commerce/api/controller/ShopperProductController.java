package commerce.api.controller;

import commerce.ProductRepository;
import commerce.SellerRepository;
import commerce.query.GetProductPage;
import commerce.querymodel.GetProductPageQueryProcessor;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@RestController
public record ShopperProductController(ProductRepository productRepository,
                                       SellerRepository sellerRepository,
                                       EntityManager entityManager) {
    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(@RequestParam(required = false) String continuationToken){

        var processor = new GetProductPageQueryProcessor(entityManager);
        GetProductPage query = new GetProductPage(continuationToken);
        return processor.process(query);
    }
}
