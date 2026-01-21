package com.erp.domain.client.service;

import com.erp.domain.accident.entity.Accident;
import com.erp.domain.accident.repository.AccidentRepository;
import com.erp.domain.client.dto.request.AddClientBlacklist;
import com.erp.domain.client.dto.response.ClientAccidentHistoryResponse;
import com.erp.domain.client.dto.response.ClientDetailResponse;
import com.erp.domain.client.dto.response.ClientSummaryResponse;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.rent.repository.RentRepository;
import com.erp.global.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ManagerClientService {

    private final ClientRepository clientRepository;

    private final AccidentRepository accidentRepository;

    private final RentRepository rentRepository;

    // 회원목록 조회
    public List<ClientSummaryResponse> getClients() {
        return clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::toSummary)
                .toList();
    }

    // 회원 검색
    public List<ClientSummaryResponse> searchClients(String keyword) {
        return clientRepository
                .searchClient(keyword)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    // 회원 상세정보
    public ClientDetailResponse getClientDetail(Long clientId) {
        Client client = getClient(clientId);
        return new ClientDetailResponse(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getBirthday(),
                client.getGender(),
                client.getLicenceNumber(),
                client.getCreatedAt(),
                client.getLicenceDay(),
                client.getBlacklisted(),
                client.getBlacklistInfo()
        );
    }

    // 회원 렌트 기록
    public List<ClientRentHistoryResponse> getRentHistory(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new CustomException(404, "회원 정보를 찾을 수 없습니다.");
        }
        return rentRepository.findRentHistoryByClient(clientId).stream()
                .map(this::toRentHistory)
                .toList();
    }

    // todo
    // 회원 사고 기록
    public List<ClientAccidentHistoryResponse> getAccidentHistory(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new CustomException(404, "회원 정보를 찾을 수 없습니다.");
        }

        return accidentRepository.findByClientId(clientId).stream()
                .map(this::toAccidentHistory)
                .toList();
    }

    // 회원 블랙리스트 추가
    @Transactional
    public void addBlacklist(Long clientId, AddClientBlacklist dto) {
        Client client = getClient(clientId);
        client.setBlacklisted(true);
        client.setBlacklistInfo(dto.blacklistInfo());
    }

    private ClientSummaryResponse toSummary(Client client) {
        return new ClientSummaryResponse(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getCreatedAt(),
                client.getBlacklisted()
        );
    }

    private ClientAccidentHistoryResponse toAccidentHistory(Accident accident) {
        return new ClientAccidentHistoryResponse(
                accident.getId(),
                accident.getTime(),
                accident.getStatus(),
                accident.getDescription(),
                accident.getLocation(),
                accident.getPart(),
                accident.getCar().getImage(),
                accident.getCar().getInsuranceName(),
                accident.getCar().getBrand(),
                accident.getCar().getModel(),
                String.valueOf(accident.getCar().getYear())
        );
    }

    private Client getClient(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "회원 정보를 찾을 수 없습니다."));
    }

    private ClientRentHistoryResponse toRentHistory(Rent rent) {
        return new ClientRentHistoryResponse(
                rent.getId(),
                rent.getCar().getCarNumber(),
                rent.getCar().getModel(),
                rent.getCar().getBrand(),
                rent.getCar().getYear(),
                rent.getRentalFee(),
                rent.getStartRentDateTime(),
                rent.getEndRentDateTime()
        );
    }

    @Transactional
    public void removeBlacklist(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "해당 고객을 찾을 수 없습니다."));

        client.setBlacklisted(false);
        client.setBlacklistInfo(null);
    }
}
