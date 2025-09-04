package commerce.querymodel;

import commerce.Product;
import commerce.result.ArrayCarrier;
import commerce.view.SellerProductView;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class GetSellerProductsQueryProcessor {

    private Function<UUID, List<Product>> findProductsOfSeller;

    public GetSellerProductsQueryProcessor(Function<UUID, List<Product>> findProductsOfSeller) {
        this.findProductsOfSeller = findProductsOfSeller;
    }

    public ArrayCarrier<SellerProductView> process(UUID uuid) {
        SellerProductView[] productViews = findProductsOfSeller.apply(uuid)
            .stream()
            .map(ProductMapper::convertToView)
            .sorted(Comparator.comparing(SellerProductView::registeredTimeUtc).reversed())
            .toArray(SellerProductView[]::new);

        return new ArrayCarrier<>(productViews);
    }
}
