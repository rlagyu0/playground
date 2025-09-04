package commerce.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.ChangeContactEmailCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static commerce.UserPropertyValidator.isEmailValid;

@RestController
public record SellerChangeContactEmailController(SellerRepository sellerRepository) {

    @PostMapping("/seller/changeContactEmail")
    ResponseEntity<?> changeContactEmail(
        @RequestBody ChangeContactEmailCommand command,
        Principal user
     ){

        if(!isEmailValid(command.contactEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Seller seller = sellerRepository.findById(UUID.fromString(user.getName())).orElseThrow();
        seller.setContactEmail(command.contactEmail());
        sellerRepository.save(seller);

        return ResponseEntity.noContent().build();
    }


}
