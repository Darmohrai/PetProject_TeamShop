package org.example.teamshop.service.CartItemService;

import org.example.teamshop.dto.CartItemDTO;
import org.example.teamshop.request.UpdateCartItemRequest;

import java.util.List;

public interface ICartItemService {
    CartItemDTO addNewCartItem(UpdateCartItemRequest request);

    CartItemDTO updateCartItem(UpdateCartItemRequest request);

    CartItemDTO getCartItemByProductId(Long productId, Long cartId);

    List<CartItemDTO> getAllCartItems(Long cartId);

    void deleteCartItem(Long productId, Long cartId);
}
