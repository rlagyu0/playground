package commerce.api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.RegisterProductCommand;
import commerce.commandmodel.RegisterProductCommandExecutor;
import commerce.query.FindSellerProduct;
import commerce.querymodel.FindSellerProductQueryProcessor;
import commerce.querymodel.GetSellerProductsQueryProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record RegisterProductController(ProductRepository productRepository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(@RequestBody RegisterProductCommand command, Principal user) {

        UUID uuid = UUID.randomUUID();
        UUID sellerId = UUID.fromString(user.getName());

        var executor = new RegisterProductCommandExecutor(productRepository::save);
        executor.execute(command, uuid, sellerId);

        URI location = URI.create("/seller/products/" + uuid);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id, Principal user) {
        var processor = new FindSellerProductQueryProcessor(productRepository::findById);
        var query = new FindSellerProduct(UUID.fromString(user.getName()), id);
        return ResponseEntity.of(processor.process(query));
    }

    @GetMapping("/seller/products")
    ResponseEntity<?> getProducts(Principal user) {
        Function<UUID, List<Product>> findProductsOfSeller = productRepository::findBySellerId;
        var processor = new GetSellerProductsQueryProcessor(findProductsOfSeller);
        return ResponseEntity.ok(processor.process(UUID.fromString(user.getName())));

    }

}
