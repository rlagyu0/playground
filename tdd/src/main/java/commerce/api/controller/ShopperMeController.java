package commerce.api.controller;

import commerce.Shopper;
import commerce.ShopperRepository;
import commerce.view.ShopperMeView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
public record ShopperMeController(JwtDecoder jwtDecoder, ShopperRepository shopperRepository) {

    @GetMapping("/shopper/me")
    ShopperMeView me(Principal user){

        UUID id = UUID.fromString(user.getName());

        Shopper shopper = shopperRepository.findById(id).orElseThrow();

        return new ShopperMeView(id, shopper.getEmail(), shopper.getUsername()) ;
    }
}
