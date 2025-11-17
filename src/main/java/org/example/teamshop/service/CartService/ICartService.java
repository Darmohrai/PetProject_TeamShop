package org.example.teamshop.service.CartService;

import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.model.Cart;

public interface ICartService {
    // return cart ID
    Long createNewCart(Long clientId);

    CartDTO getCartDTOById(Long id);

    Cart getCartEntityById(Long clientId);

    Cart getCartEntityByClientId(Long clientId);

    CartDTO getCartDTOByClientId(Long clientId);

    void deleteCart(Long id);
}
