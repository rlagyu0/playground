package test.commerce;

import commerce.command.RegisterProductCommand;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterProductCommandGenerator {

    public static RegisterProductCommand generateRegisterProductCommand() {
        return new RegisterProductCommand(
            generateProductName(),
            generateProductImageUri(),
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }

    private static String generateProductName() {
        return "name"+ UUID.randomUUID().toString();
    }

    private static String generateProductImageUri() {
        return "https://test.com/images/" + UUID.randomUUID().toString();
    }

    private static String generateProductDescription() {
        return "description" + UUID.randomUUID().toString();
    }

    private static BigDecimal generateProductPriceAmount() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return BigDecimal.valueOf(random.nextInt(10000,100000));
    }

    private static int generateProductStockQuantity() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(10,100);
    }

    public static RegisterProductCommand generateRegisterProductCommand(String imageUri) {
        return new RegisterProductCommand(
            generateProductName(),
            imageUri,
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }
}
