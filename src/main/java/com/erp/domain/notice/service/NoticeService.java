package com.erp.domain.notice.service;

import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.domain.notice.dto.request.NoticeCreateDto;
import com.erp.domain.notice.entity.Notice;
import com.erp.domain.notice.repository.NoticeRepository;
import com.erp.global.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final EmployeeRepository employeeRepository;

    public void createNotice(Long employeeId, @Valid NoticeCreateDto request) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new CustomException(404, "직원을 찾을 수 없습니다.")
        );

        String summary = toSummary(request.content());

        noticeRepository.save(Notice.builder()
                .employee(employee)
                .employeeName(employee.getName())
                .title(request.title())
                .content(request.content())
                .summary(summary)
                .active(request.active())
                .pinned(request.pinned())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build());
    }

    private String toSummary(String content) {
        return content.substring(0, Math.min(content.length(), 90));
    }

    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
