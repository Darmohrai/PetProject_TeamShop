package org.example.teamshop.service.CartService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.mapper.CartMapper;
import org.example.teamshop.model.Cart;
import org.example.teamshop.repository.CartRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public Long createNewCart(Long clientId) {
        Cart cart = new Cart();
        cart.setClientId(clientId);
        cartRepository.save(cart);
        return cart.getId();
    }

    @Override
    public CartDTO findCartById(Long id) {
        Cart cart = findCartEntityByClientId(id);
        return cartMapper.toCartDTO(cart);
    }

    @Override
    public Cart findCartEntityByClientId(Long clientId) {
        return cartRepository
                .findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Override
    public void deleteCart(Long id) {
        Cart cart = findCartEntityByClientId(id);
        cartRepository.delete(cart);
    }
}
