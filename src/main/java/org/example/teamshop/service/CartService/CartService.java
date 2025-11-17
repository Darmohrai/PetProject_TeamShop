package org.example.teamshop.service.CartService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.mapper.CartMapper;
import org.example.teamshop.model.Cart;
import org.example.teamshop.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public Long createNewCart(Long clientId) {
        if (clientId == null)
            throw new IllegalArgumentException("Client Id cannot be null");

        Cart cart = new Cart();
        cart.setClientId(clientId);
        cartRepository.save(cart);
        return cart.getId();
    }

    @Override
    public CartDTO getCartDTOById(Long id) {
        Cart cart = getCartEntityById(id);
        return cartMapper.toCartDTO(cart);
    }

    @Override
    public Cart getCartEntityById(Long cartId) {
        if (cartId == null)
            throw new IllegalArgumentException("Cart Id cannot be null");

        return cartRepository
                .findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Override
    public Cart getCartEntityByClientId(Long clientId) {
        return cartRepository.findByClientId(clientId);
    }

    @Override
    public CartDTO getCartDTOByClientId(Long clientId) {
        Cart cart = cartRepository.findByClientId(clientId);
        return cartMapper.toCartDTO(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long id) {
        Cart cart = getCartEntityById(id);
        cartRepository.delete(cart);
    }
}
