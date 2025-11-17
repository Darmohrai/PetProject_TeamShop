package org.example.teamshop.service.ClientService;

import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;

public interface IClientService {

    ClientDTO getClientById(Long id);

    ClientDTO addClient(CreateClientRequest createClientRequest);

    ClientDTO updateClient(UpdateClientRequest updateClientRequest, Long id);

    void deleteClient(Long id);

    ClientDTO getClientByEmail(String email);

    Long getAuthorizedClientId();

    String getAuthorizedClientEmail();
}
