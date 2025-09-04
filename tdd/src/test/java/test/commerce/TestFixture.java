package test.commerce;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import commerce.ProductRepository;
import commerce.command.CreateSellerCommand;
import commerce.command.CreateShopperCommand;
import commerce.command.RegisterProductCommand;
import commerce.query.IssueSellerToken;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import commerce.view.SellerMeView;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

public record TestFixture(TestRestTemplate client, ProductRepository productRepository) {

    public static TestFixture create(Environment env, ProductRepository productRepository) {
        TestRestTemplate client = new TestRestTemplate();
        client.setUriTemplateHandler(new LocalHostUriTemplateHandler(env));
        return new TestFixture(client, productRepository);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
//        client.postForEntity("/shopper/signup", command, Void.class);
        ensureSuccessful(
            client.postForEntity("/shopper/signup", command, Void.class),
            command
        );
    }

    private void ensureSuccessful(ResponseEntity<Void> response, Object request) {
        if(!response.getStatusCode().is2xxSuccessful()) {
            String message = "Request with " + request + " failed with status code " + response.getStatusCode();
            throw new RuntimeException(message);
        }
    }

    public String issueShopperToken(String email, String password) {
        return Objects.requireNonNull(client.postForEntity(
            "/shopper/issuetoken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        ).getBody()).accessToken();
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();

        createShopper(email, generateUsername(), password);
        return issueShopperToken(email, password);
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        String headerValue = "Bearer " + token;
        setDefaultAuthorization(headerValue);
    }

    private void setDefaultAuthorization(String headerValue) {
        RestTemplate restTemplate = client.getRestTemplate();
        restTemplate.getInterceptors().addFirst(
            (request, body, execution) -> {
                if (request.getHeaders().containsKey("Authorization") == false) {
                    request.getHeaders().add("Authorization", headerValue);
                }
                return execution.execute(request, body);
            }
        );
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createSeller(email, generateUsername(), password, generateEmail());
        setSellerAsDefaultUser(email, password);
    }

    public void createSeller(String email, String username, String password, String contactEmail) {
        var command = new CreateSellerCommand(email, username, password, contactEmail);
//        client.postForEntity("/seller/signup", command, Void.class);
        ensureSuccessful(
            client.postForEntity("/seller/signup", command, Void.class), command
        );
    }

    public void setSellerAsDefaultUser(String email, String password) {
        String token = issueSellerToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private String issueSellerToken(String email, String password) {
        return Objects.requireNonNull(client.postForEntity(
            "/seller/issuetoken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class
        ).getBody()).accessToken();
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperAsDefaultUser(email, password);
    }

    public UUID registerProduct() {
        return registerProduct(RegisterProductCommandGenerator.generateRegisterProductCommand());
    }

    public UUID registerProduct(RegisterProductCommand command) {
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/products",
                command,
            Void.class
        );

        URI location = response.getHeaders().getLocation();
        String id = Objects.requireNonNull(location).getPath().substring("/seller/products/".length());
        UUID uuid = UUID.fromString(id);
        return uuid;
    }
    public List<UUID> registerProducts() {
        return List.of(registerProduct(), registerProduct(), registerProduct());
    }

    public List<UUID> registerProducts(int count) {
        ArrayList<UUID> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(registerProduct());
        }

        return ids;
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }

    public String consumeProductPage() {
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                RequestEntity.get("/shopper/products").build(),
                new ParameterizedTypeReference<>() { }
        );

        return Objects.requireNonNull(response.getBody()).continuationToken();
    }

    public String consumeTwoProductPage() {
        String token = consumeProductPage();
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
                RequestEntity.get("/shopper/products?continuationToken="+token).build(),
                new ParameterizedTypeReference<>() { }
        );

        return Objects.requireNonNull(response.getBody()).continuationToken();

    }
}
