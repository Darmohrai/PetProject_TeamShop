package org.example.teamshop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdminRequest {
    @Email
    private String email;
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "incorrect phone format")
    private String phone;
}
