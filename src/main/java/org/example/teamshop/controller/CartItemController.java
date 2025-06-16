package org.example.teamshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.annotation.PermissionCheck.PermissionCheck;
import org.example.teamshop.dto.CartItemDTO;
import org.example.teamshop.request.UpdateCartItemRequest;
import org.example.teamshop.service.CartItemService.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/client/cart/cart_item/")
public class CartItemController {
    private final CartItemService cartItemService;

    @Operation(summary = "Find cart item by product id", description = "Find item in customers cart by product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/{productId}")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<CartItemDTO> getCartItemByProductId(@Parameter(description = "Id of product")
                                                              @PathVariable Long productId,
                                                              @RequestBody Long clientId) {
        CartItemDTO cartItemDTO = cartItemService.getCartItemByProductId(productId);
        return ResponseEntity.ok(cartItemDTO);
    }

    @Operation(summary = "Update cart item", description = "Update the quantity of a specific item in the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @PutMapping("/put")
    @PermissionCheck("@permissionHandler.hasPermissionForCart(#updateCartItemRequest.cartId)")
    public ResponseEntity<CartItemDTO> putCartItem(@RequestBody @Valid UpdateCartItemRequest updateCartItemRequest) {
        CartItemDTO cartItemDTO = cartItemService.updateCartItem(updateCartItemRequest);
        return ResponseEntity.ok().body(cartItemDTO);
    }

    @Operation(summary = "Get all cart items", description = "Retrieve all items in a specific cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart items successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/all")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@RequestBody Long clientId) {
        List<CartItemDTO> itemDTOS = cartItemService.getAllCartItems(clientId);
        return ResponseEntity.ok(itemDTOS);
    }

    @Operation(summary = "Delete cart item", description = "Remove a specific item from the cart by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{productId}")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<String> deleteCartItem(@Parameter(description = "Id of product") @PathVariable Long productId,
                                                 @RequestBody Long clientId) {
        cartItemService.deleteCartItem(productId);
        return ResponseEntity.ok().build();
    }
}
