package org.example.teamshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.CartDTO;
import org.example.teamshop.service.CartService.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/cart/")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Find cart by client ID", description = "Retrieve cart details by client unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart successfully found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<CartDTO> getCartByClientId(@Parameter(description = "ID of the client to be fetched", required = true, example = "1")
                                                     @PathVariable final Long clientId) {
        try {
            CartDTO cartDTO = cartService.findCartById(clientId);
            return ResponseEntity.ok(cartDTO);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
