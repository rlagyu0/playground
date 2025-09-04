package commerce.api.controller;

import javax.crypto.spec.SecretKeySpec;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.api.JwtKeyHolder;
import commerce.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerIssueTokenController(@Value("${security.jwt.secret}") String jwtSecret,
                                         SellerRepository sellerRepository,
                                         PasswordEncoder passwordEncoder,
                                         JwtKeyHolder jwtKeyHolder) {
    @PostMapping("/seller/issuetoken")
    ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerToken query) {

        return sellerRepository.findByEmail(query.email())
            .filter(seller -> passwordEncoder.matches(query.password(), seller.getHashedPassword()))
            .map(this::composeToken)
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    private String composeToken(Seller seller) {
        return Jwts.builder()
            .setSubject(seller.getId().toString())
            .signWith(jwtKeyHolder.key())
            .claim("scp", "seller")
            .compact();
    }
}
