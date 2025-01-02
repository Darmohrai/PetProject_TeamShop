package org.example.teamshop.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private int cartId;
    private int orderId;
}
