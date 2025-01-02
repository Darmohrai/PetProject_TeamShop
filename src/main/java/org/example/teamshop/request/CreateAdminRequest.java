package org.example.teamshop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminRequest {
    @NotNull
    private String name;
//    At least 8 characters.
//    Contains at least one uppercase letter.
//    Contains at least one lowercase letter.
//    Contains at least one digit.
//    Contains at least one special character (e.g., !@#$%^&*).
//    No spaces allowed.
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Not valid Password format")
    private String password;
    @Email
    @NotEmpty
    private String email;
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "incorrect phone format")
    private String phone;
}
