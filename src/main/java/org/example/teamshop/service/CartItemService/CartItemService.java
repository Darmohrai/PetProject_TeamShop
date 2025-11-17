package org.example.teamshop.service.CartItemService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.dto.CartItemDTO;
import org.example.teamshop.mapper.CartItemMapper;
import org.example.teamshop.model.Cart;
import org.example.teamshop.model.CartItem;
import org.example.teamshop.repository.CartItemRepository;
import org.example.teamshop.repository.CartRepository;
import org.example.teamshop.repository.ProductRepository;
import org.example.teamshop.request.UpdateCartItemRequest;
import org.example.teamshop.service.CartService.CartService;
import org.example.teamshop.service.ClientService.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ClientService clientService;


    @Override
    @Transactional
    public CartItemDTO addNewCartItem(UpdateCartItemRequest request) {
        if (!productRepository.existsById(request.getProductId()))
            throw new EntityNotFoundException("Product with ID " + request.getProductId() + " not found");
        Cart cart = cartService.getCartEntityById(request.getCartId());
        CartItem cartItem = cartItemMapper.toCartItemFromUpdateCartItemRequest(request);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
        cart.getItems().add(cartItem);
        cartRepository.save(cart);
        return cartItemMapper.toCartItemDTO(cartItem);
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItem(UpdateCartItemRequest request) {
        Cart cart = cartService.getCartEntityById(request.getCartId());
        if (request.getQuantity() == 0) {
            deleteCartItem(request.getProductId());
            return null;
        }
        if (cart.getItems().isEmpty()) return addNewCartItem(request);
        else {
            for (CartItem cartItem1 : cart.getItems()) {
                if (cartItem1.getProductId().equals(request.getProductId())) {
                    cartItem1.setQuantity(request.getQuantity());
                    cartRepository.save(cart);
                    return cartItemMapper.toCartItemDTO(cartItem1);
                }
            }
        }
        return addNewCartItem(request);
    }

    @Override
    public CartItemDTO getCartItemByProductId(Long productId) {
        CartDTO cartDTO = cartService.getCartDTOByClientId(clientService.getAuthorizedClientId());
        return cartDTO.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("There is no such type of product in this cart"));
    }

    @Override
    public List<CartItemDTO> getAllCartItems(Long clientId) {
        CartDTO cartDTO = cartService.getCartDTOByClientId(clientId);
        return cartDTO.getItems();
    }

    @Override
    @Transactional
    public void deleteCartItem(Long productId) {
        Cart cart = cartService.getCartEntityByClientId(clientService.getAuthorizedClientId());
        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getProductId().equals(productId)) {
                iterator.remove();
                cartItemRepository.delete(cartItem);
                cartRepository.save(cart);
                return;
            }
        }
    }
}
