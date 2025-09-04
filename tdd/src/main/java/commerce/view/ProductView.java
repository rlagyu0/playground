package commerce.view;

import commerce.Seller;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductView(UUID id, SellerView seller,
                          String name,
                          String imageUri,
                          String description,
                          BigDecimal priceAmount,
                          int stockQuantity) {
}
