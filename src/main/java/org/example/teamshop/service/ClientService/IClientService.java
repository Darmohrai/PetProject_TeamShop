package org.example.teamshop.service.ClientService;

import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;

public interface IClientService {

    ClientDTO findClientById(Long id);

    ClientDTO addClient(CreateClientRequest createClientRequest);

    ClientDTO updateClient(UpdateClientRequest updateClientRequest, Long id);

    void deleteClient(Long id);
}
