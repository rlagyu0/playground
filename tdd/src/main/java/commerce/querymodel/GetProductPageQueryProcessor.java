package commerce.querymodel;

import commerce.query.GetProductPage;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import jakarta.persistence.EntityManager;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class GetProductPageQueryProcessor {

    private final EntityManager entityManager;

    public GetProductPageQueryProcessor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public PageCarrier<ProductView> process(GetProductPage query) {
        String queryString = """
            SELECT
                new commerce.querymodel.ProductSellerTuple(p,s) 
            FROM Product p 
            JOIN Seller s ON p.sellerId = s.id 
            WHERE :cursor IS NULL OR p.dataKey <= :cursor
            ORDER BY p.dataKey DESC
            """;

        List<ProductSellerTuple> results = entityManager
            .createQuery(queryString, ProductSellerTuple.class)
            .setParameter("cursor", decodeCursor(query.continuationToken()))
            .setMaxResults(10 + 1)
            .getResultList();

        ProductView[] array = (ProductView[]) results
            .stream()
            .limit(10)
            .map(ProductSellerTuple::toView)
            .toArray(ProductView[]::new);

        Long next = results.size() <= 10 ? null : results.getLast().product().getDataKey();

        return new PageCarrier<>(array, encodeCursor(next));
    }

    private Object decodeCursor(String continuationToken) {
        if(Objects.isNull(continuationToken)) {
            return null;
        }
        byte[] data = Base64.getUrlDecoder().decode(continuationToken);
        return Long.parseLong(new String(data, StandardCharsets.UTF_8));
    }

    private String encodeCursor(Long nextCursor) {
        if(Objects.isNull(nextCursor)) {
            return null;
        }
        byte[] data = nextCursor.toString().getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
