package org.example.teamshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.annotation.PermissionCheck.PermissionCheck;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.request.UpdateClientRequest;
import org.example.teamshop.service.ClientService.IClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/client/")
public class ClientController {
    private final IClientService clientService;

    @Operation(summary = "Find client by ID", description = "Retrieve client details by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "403", description = "User is denied access to this ID"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{clientId}")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<ClientDTO> getClientById(
            @Parameter(description = "ID of the client to be fetched", required = true, example = "1")
            @PathVariable Long clientId) {
        ClientDTO clientDTO = clientService.findClientById(clientId);
        return ResponseEntity.ok(clientDTO);
    }

    @Operation(summary = "Update an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided in the request body or client ID is invalid."),
            @ApiResponse(responseCode = "403", description = "User is denied access to this ID"),
            @ApiResponse(responseCode = "404", description = "Client with the provided ID not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong on the server side.")
    })
    @PutMapping("/{clientId}")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<ClientDTO> updateClient(
            @Parameter(description = "The client ID to update") @PathVariable Long clientId,
            @Parameter(description = "The client data to update", required = true) @RequestBody @Valid UpdateClientRequest updateClientRequest) {
        ClientDTO clientDTO = clientService.updateClient(updateClientRequest, clientId);
        return ResponseEntity.ok(clientDTO);
    }

    @Operation(summary = "Delete a client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client successfully deleted. No content returned."),
            @ApiResponse(responseCode = "403", description = "User is denied access to this ID"),
            @ApiResponse(responseCode = "404", description = "Client with the provided ID not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong on the server side.")
    })
    @DeleteMapping("/{clientId}")
    @PermissionCheck("@permissionHandler.hasPermissionByClientId(#clientId)")
    public ResponseEntity<ClientDTO> deleteClient(
            @Parameter(description = "The client ID to delete", required = true) @PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }
}
