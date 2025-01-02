package org.example.teamshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ClientDTO;
import org.example.teamshop.request.CreateClientRequest;
import org.example.teamshop.request.UpdateClientRequest;
import org.example.teamshop.service.IClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/client")
public class ClientController {
    private final IClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO clientDTO = clientService.findClientById(id);
        return ResponseEntity.ok(clientDTO);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody CreateClientRequest createClientRequest) {
        ClientDTO clientDTO = clientService.addClient(createClientRequest);
        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@RequestBody UpdateClientRequest updateClientRequest, @PathVariable Long id) {
        ClientDTO clientDTO = clientService.updateClient(updateClientRequest, id);
        return ResponseEntity.ok(clientDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientDTO> deleteClient(@PathVariable Long id) {
        ClientDTO clientDTO = clientService.findClientById(id);
        if (clientDTO != null) {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
