package org.example.teamshop.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequest {
    @NotNull
    private Long cartId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
