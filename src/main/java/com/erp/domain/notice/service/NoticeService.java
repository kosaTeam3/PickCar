package com.erp.domain.notice.service;

import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.domain.notice.dto.NoticeSummaryResponse;
import com.erp.domain.notice.dto.request.NoticeCreateDto;
import com.erp.domain.notice.dto.request.NoticeSearchDto;
import com.erp.domain.notice.dto.request.NoticeUpdateDto;
import com.erp.domain.notice.entity.Notice;
import com.erp.domain.notice.repository.NoticeRepository;
import com.erp.global.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final EmployeeRepository employeeRepository;

    public void createNotice(Long employeeId, @Valid NoticeCreateDto request) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new CustomException(404, "직원을 찾을 수 없습니다.")
        );

        if (request.startDate() != null && request.endDate() != null) {
            if (request.startDate().isAfter(request.endDate())) {
                throw new CustomException(400, "게시 시작일은 종료일보다 늦을 수 없습니다.");
            }
        }

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
        if (content == null)
            return null;

        return content.substring(0, Math.min(content.length(), 90));
    }

    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    public void updateNotice(Long employeeId, NoticeUpdateDto request, Long noticeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new CustomException(404, "직원을 찾을 수 없습니다.");
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                () -> new CustomException(404, "공지사항을 찾을 수 없습니다.")
        );

        if (request.startDate() != null && request.endDate() != null) {
            if (request.startDate().isAfter(request.endDate())) {
                throw new CustomException(400, "게시 시작일은 종료일보다 늦을 수 없습니다.");
            }
        }

        if (request.title() != null) {
            notice.setTitle(request.title());
        }

        if (request.content() != null) {
            notice.setContent(request.content());
            notice.setSummary(toSummary(request.content()));
        }

        if (request.active() != null) {
            notice.setActive(request.active());
        }

        if (request.pinned() != null) {
            notice.setPinned(request.pinned());
        }

        if (request.startDate() != null) {
            notice.setStartDate(request.startDate());
        }

        if (request.endDate() != null) {
            notice.setEndDate(request.endDate());
        }
    }

    public Page<NoticeSummaryResponse> getNotices(NoticeSearchDto search, Pageable pageable) {
        return noticeRepository.searchSummaries(search.title(), search.writer(), search.active(), pageable);
    }
}
