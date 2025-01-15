package org.example.teamshop.mapper;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.model.Cart;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);

    Cart toCart(CartDTO cartDTO);
}
