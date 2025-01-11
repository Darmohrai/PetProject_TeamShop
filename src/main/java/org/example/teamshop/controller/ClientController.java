package org.example.teamshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;
import org.example.teamshop.service.IClientService;
import org.springframework.http.HttpStatus;
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
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(
            @Parameter(description = "ID of the client to be fetched", required = true, example = "1")
            @PathVariable Long id) {
        try {
            ClientDTO clientDTO = clientService.findClientById(id);
            return ResponseEntity.ok(clientDTO);
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Add client to DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully added to the database"),
            @ApiResponse(responseCode = "400", description = "Invalid client data provided. The request body is missing required fields or contains invalid data."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong on the server side.")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @Parameter(description = "JSON file of client (without ID)", required = true)
            @RequestBody @Valid CreateClientRequest createClientRequest) {
        try {
            ClientDTO clientDTO = clientService.addClient(createClientRequest);
            return ResponseEntity.ok(clientDTO);
        }
        catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().equals("A client with this email already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            else if (e.getMessage().equals("Conflict! Bad request")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided in the request body or client ID is invalid."),
            @ApiResponse(responseCode = "404", description = "Client with the provided ID not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong on the server side.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @Parameter(description = "The client data to update", required = true) @RequestBody UpdateClientRequest updateClientRequest,
            @Parameter(description = "The client ID to update") @PathVariable Long id) {
        ClientDTO clientDTO = clientService.updateClient(updateClientRequest, id);
        return ResponseEntity.ok(clientDTO);
    }

    @Operation(summary = "Delete a client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client successfully deleted. No content returned."),
            @ApiResponse(responseCode = "404", description = "Client with the provided ID not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong on the server side.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientDTO> deleteClient(
            @Parameter(description = "The client ID to delete", required = true) @PathVariable Long id) {
        ClientDTO clientDTO = clientService.findClientById(id);
        if (clientDTO != null) {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
