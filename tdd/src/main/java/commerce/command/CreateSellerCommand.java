package commerce.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateSellerCommand(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    String email,
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min=3, max=100)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "특수문자는 허용되지 않습니다.")
    String username,
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min=8, max=100, message = "비밀번호는 8자 이상 100자 이하이어야 합니다.")
    String password,
    @NotBlank(message = "문의 이메일은 필수입니다.")
    @Email(message = "유효하지 않은 문의 이메일 형식입니다.")
    String contactEmail) {
}
