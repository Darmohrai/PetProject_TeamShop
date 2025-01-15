package org.example.teamshop.mapper;

import org.example.teamshop.dto.CartItemDTO;
import org.example.teamshop.model.CartItem;
import org.example.teamshop.request.UpdateCartItemRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItemFromUpdateCartItemRequest(UpdateCartItemRequest updateCartItemRequest);

    CartItemDTO toCartItemDTO(CartItem cartItem);

    CartItem toCartItem(CartItemDTO cartItemDTO);
}
