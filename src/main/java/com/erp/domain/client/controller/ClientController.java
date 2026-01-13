package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.AddClientBlacklist;
import com.erp.domain.client.dto.responce.ClientAccidentHistoryResponse;
import com.erp.domain.client.dto.responce.ClientDetailResponse;
import com.erp.domain.client.dto.responce.ClientRentHistoryResponse;
import com.erp.domain.client.dto.responce.ClientSummaryResponse;
import com.erp.domain.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @GetMapping(params = "keyword")
    public ResponseEntity<List<ClientSummaryResponse>> searchClients(@RequestParam String keyword) {
        return ResponseEntity.ok(clientService.searchClients(keyword));
    }

    @GetMapping
    public ResponseEntity<List<ClientSummaryResponse>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDetailResponse> getClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getClientDetail(clientId));
    }

    @GetMapping("/rent/{clientId}")
    public ResponseEntity<List<ClientRentHistoryResponse>> getRentHistory(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getRentHistory(clientId));
    }

    @GetMapping("/accident/{clientId}")
    public ResponseEntity<List<ClientAccidentHistoryResponse>> getAccidentHistory(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getAccidentHistory(clientId));
    }

    @PostMapping("/black/{clientId}")
    public ResponseEntity<Void> addBlacklist(@PathVariable Long clientId, @Valid @RequestBody AddClientBlacklist dto) {
        clientService.addBlacklist(clientId, dto);
        return ResponseEntity.noContent().build();
    }
}
