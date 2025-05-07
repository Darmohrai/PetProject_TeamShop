package org.example.teamshop.service.ClientService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.AlreadyExistingResourceException;
import org.example.teamshop.Exception.FailedOperationException;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.mapper.ClientMapper;
import org.example.teamshop.model.Client;
import org.example.teamshop.repository.ClientRepository;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;
import org.example.teamshop.service.CartService.CartService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;
    private final CartService cartService;
    private final ClientMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientDTO findClientById(Long id) {
        return clientRepository.findById(id)
                .map(mapper::clientToClientDTO)
                .orElseThrow(() -> new EntityNotFoundException("Client with ID " + id + " not found"));
    }

    @Override
    public ClientDTO addClient(CreateClientRequest createClientRequest) {
        if (clientRepository.existsByEmail(createClientRequest.getEmail())) {
            throw new AlreadyExistingResourceException("A client with this email already exists");
        }
        try {
            Client newClient = mapper.createClientRequestToClient(createClientRequest);
            newClient.setPassword(passwordEncoder.encode(createClientRequest.getPassword()));
            clientRepository.save(newClient);
            Long cartId = cartService.createNewCart(newClient.getId());
            newClient.setCartId(cartId);
            clientRepository.save(newClient);
            return mapper.clientToClientDTO(newClient);
        } catch (IllegalArgumentException e) {
            throw new FailedOperationException("Bad request");
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new FailedOperationException("Conflict! Bad request");
        }
    }


    @Override
    public ClientDTO updateClient(UpdateClientRequest updateClientRequest, Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        mapper.updateClientRequestToClient(updateClientRequest, client);
        clientRepository.save(client);
        return mapper.clientToClientDTO(client);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
        cartService.deleteCart(client.getCartId());
        clientRepository.delete(client);
    }

    public ClientDTO findClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        return mapper.clientToClientDTO(client);
    }

    public Long findAuthorizedClientId() {
        Client client = clientRepository.findByEmail(findAuthorizedClientEmail());
        return client.getId();
    }

    public String findAuthorizedClientEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
