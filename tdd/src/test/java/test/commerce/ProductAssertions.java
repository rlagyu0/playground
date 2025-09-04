package test.commerce;

import commerce.command.RegisterProductCommand;
import commerce.view.ProductView;
import commerce.view.SellerProductView;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<? super SellerProductView> isDerivedFrom(RegisterProductCommand command) {
        return product -> {
            assertThat(product).isNotNull();
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount()).matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity()).isEqualTo(command.stockQuantity());
        };
    }

    private static Predicate<? super BigDecimal> equals(BigDecimal priceAmount) {
        return price -> price.compareTo(priceAmount) == 0;
    }

    public static ThrowingConsumer<? super ProductView> isViewDerivedFrom(RegisterProductCommand command) {
        return product -> {
            assertThat(product).isNotNull();
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount()).matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity()).isEqualTo(command.stockQuantity());
        };
    }
}
