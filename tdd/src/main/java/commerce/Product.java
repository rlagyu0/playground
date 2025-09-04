package commerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(indexes = @Index(columnList = "sellerId"))
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    private UUID sellerId;

    private String name;
    private String description;
    private String imageUri;
    private BigDecimal priceAmount;
    private int stockQuantity;
    private LocalDateTime registeredTimeUtc;

}
