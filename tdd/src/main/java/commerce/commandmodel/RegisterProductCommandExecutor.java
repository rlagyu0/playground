package commerce.commandmodel;

// 자바 하위 패키지와 프로젝트에서 정의한 엔티티와 메소드만 의존하고 있다.
// 프로덕트 엔티티를 통해서 jpa 즉, 데이터 접근에 대한 사실상 표준 추상화에 간접적으로 의존
// 기반구조나 응용프로그램 프레임 워크에 의존하지 않는다.
// 가볍기 때문에 다양한 구조를 가질 수 있다.
import commerce.Product;
import commerce.command.RegisterProductCommand;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static java.time.ZoneOffset.UTC;

public class RegisterProductCommandExecutor {

    private final Consumer<Product> saveProduct;

    public RegisterProductCommandExecutor(Consumer<Product> saveProduct) {
        this.saveProduct = saveProduct;
    }

    public void execute(RegisterProductCommand command, UUID uuid, UUID sellerId) {
        validateCommand(command);
        Product product = createProduct(command, uuid, sellerId);
        saveProduct(product);
    }

    private static void validateCommand(RegisterProductCommand command) {
        if (!isValidUri(command.imageUri())) {
            throw new InvalidCommandException();
        }
    }

    private static Product createProduct(RegisterProductCommand command, UUID uuid, UUID sellerId) {
        Product product = new Product();
        product.setId(uuid);
        product.setSellerId(sellerId);
        product.setName(command.name());
        product.setImageUri(command.imageUri());
        product.setDescription(command.description());
        product.setPriceAmount(command.priceAmount());
        product.setStockQuantity(command.stockQuantity());
        product.setRegisteredTimeUtc(LocalDateTime.now(UTC));
        return product;
    }

    private void saveProduct(Product product) {
        saveProduct.accept(product);
    }

    private static boolean isValidUri(String uri) {
        try {
            URI createUri = URI.create(uri);
            return createUri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
