package org.example.teamshop.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.teamshop.model.Category;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    @Positive
    private BigDecimal price;
    private Category category;
}
