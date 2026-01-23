package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.AddClientBlacklist;
import com.erp.domain.client.dto.response.ClientAccidentHistoryResponse;
import com.erp.domain.client.dto.response.ClientDetailResponse;
import com.erp.domain.client.dto.response.ClientRentHistoryResponse;
import com.erp.domain.client.dto.response.ClientSummaryResponse;
import com.erp.domain.client.service.ManagerClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/client")
@RequiredArgsConstructor
public class ManagerClientController {

    private final ManagerClientService clientService;


    @GetMapping(params = "keyword")
    public ResponseEntity<Page<ClientSummaryResponse>> searchClients(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageRequest,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(clientService.searchClients(keyword, pageRequest));
    }

    @GetMapping
    public ResponseEntity<Page<ClientSummaryResponse>> getClients(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageRequest) {
        return ResponseEntity.ok(clientService.getClients(pageRequest));
    }

    @GetMapping("/{clientId}")
    public ClientDetailResponse getClient(@PathVariable Long clientId) {
        return clientService.getClientDetail(clientId);
    }

    // 렌트 이력 조회
    @GetMapping("/rent/{clientId}")
    public List<ClientRentHistoryResponse> getRentHistory(@PathVariable Long clientId) {
        return clientService.getRentHistory(clientId);
    }

    // 사고 이력 조회
    @GetMapping("/accident/{clientId}")
    public List<ClientAccidentHistoryResponse> getAccidentHistory(@PathVariable Long clientId) {
        return clientService.getAccidentHistory(clientId);
    }

    @PostMapping("/black/{clientId}")
    public ResponseEntity<Void> addBlacklist(@PathVariable Long clientId, @Valid @RequestBody AddClientBlacklist dto) {
        clientService.addBlacklist(clientId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/black/{clientId}")
    public ResponseEntity<Void> removeBlacklist(@PathVariable Long clientId) {
        clientService.removeBlacklist(clientId);
        return ResponseEntity.noContent().build();
    }

}
