package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.AddClientBlacklist;
import com.erp.domain.client.dto.response.ClientAccidentHistoryResponse;
import com.erp.domain.client.dto.response.ClientDetailResponse;
import com.erp.domain.client.dto.response.ClientRentHistoryResponse;
import com.erp.domain.client.dto.response.ClientSummaryResponse;
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
    public List<ClientSummaryResponse> searchClients(@RequestParam String keyword) {
        return clientService.searchClients(keyword);
    }

    @GetMapping
    public List<ClientSummaryResponse> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/{clientId}")
    public ClientDetailResponse getClient(@PathVariable Long clientId) {
        return clientService.getClientDetail(clientId);
    }

    @GetMapping("/rent/{clientId}")
    public List<ClientRentHistoryResponse> getRentHistory(@PathVariable Long clientId) {
        return clientService.getRentHistory(clientId);
    }

    @GetMapping("/accident/{clientId}")
    public List<ClientAccidentHistoryResponse> getAccidentHistory(@PathVariable Long clientId) {
        return clientService.getAccidentHistory(clientId);
    }

    @PostMapping("/black/{clientId}")
    public ResponseEntity<Void> addBlacklist(@PathVariable Long clientId, @Valid @RequestBody AddClientBlacklist dto) {
        clientService.addBlacklist(clientId, dto);
        return ResponseEntity.noContent().build();
    }
}
