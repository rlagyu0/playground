package commerce.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public record SellerSignUpController(SellerRepository repository, PasswordEncoder passwordEncoder) {

    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp(@RequestBody @Validated CreateSellerCommand command,
                             BindingResult bindingResult
                             ) {
        // 이메일 에러
        if (isValidCommand(bindingResult)) {
            return ResponseEntity.badRequest().build();
        }

        Seller seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(passwordEncoder.encode(command.password()));
        seller.setId(UUID.randomUUID());
        seller.setContactEmail(command.contactEmail());

        try {
            repository.save(seller);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    private static boolean isValidCommand(BindingResult bindingResult) {
        return bindingResult.hasErrors()
            && (bindingResult.hasFieldErrors("email")
            || bindingResult.hasFieldErrors("username")
            || bindingResult.hasFieldErrors("password")
            || bindingResult.hasFieldErrors("contactEmail")
        );
    }
}
