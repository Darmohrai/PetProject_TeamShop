package org.example.teamshop.mapper;

import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.model.Client;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client createClientRequestToClient(CreateClientRequest request);

    ClientDTO clientToClientDTO(Client client);

    void updateClientRequestToClient(UpdateClientRequest request, @MappingTarget Client client);
}
