package org.example.teamshop.mapper;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.model.Client;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;
import org.mapstruct.Mapping;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClientMapper {
    private final ModelMapper modelMapper;

    public Client createClientRequestToClient(CreateClientRequest request) {
        return modelMapper.map(request, Client.class);
    }

    public ClientDTO clientToClientDTO(Client client) {
        return modelMapper.map(client, ClientDTO.class);
    }

    public void updateClientRequestToClient(UpdateClientRequest request, Client client) {
        if (request.getEmail() != null) {
            client.setEmail(request.getEmail());
        }
    }
}
