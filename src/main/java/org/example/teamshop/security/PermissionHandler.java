package org.example.teamshop.security;

import lombok.AllArgsConstructor;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.service.ClientService.ClientService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PermissionHandler {
    private final ClientService clientService;

    public UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public boolean hasPermissionForCart(Long cartId) {
        ClientDTO clientDTO = getCurrentClient();
        return clientDTO.getCartId().equals(cartId);
    }

    public boolean hasPermissionByClientId(Long clientId) {
        ClientDTO clientDTO = getCurrentClient();
        return clientDTO.getId().equals(clientId);
    }

    public ClientDTO getCurrentClient() {
        UserDetails user = getCurrentUser();
        return clientService.findClientByEmail(user.getUsername());
    }


}
