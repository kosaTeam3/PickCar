package com.erp.domain.notice.controller;

import com.erp.domain.notice.dto.request.NoticeCreateDto;
import com.erp.domain.notice.dto.request.NoticeSearchDto;
import com.erp.domain.notice.dto.request.NoticeUpdateDto;
import com.erp.domain.notice.dto.response.NoticeDetailDto;
import com.erp.domain.notice.dto.response.NoticeSummaryDto;
import com.erp.domain.notice.service.NoticeService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
@RequestMapping("/api/manager/notices")
@RestController
@RequiredArgsConstructor
public class NoticeManagerController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<Void> createNotice(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody NoticeCreateDto request
    ) {
        noticeService.createNotice(Long.parseLong(user.getName()), request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody NoticeUpdateDto request,
            @PathVariable Long noticeId
    ) {
        noticeService.updateNotice(Long.parseLong(user.getName()), request, noticeId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailDto> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @GetMapping
    public ResponseEntity<Page<NoticeSummaryDto>> searchNotices(
            @ModelAttribute NoticeSearchDto search,
            @PageableDefault(size = 20, sort = "id",
                    direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(noticeService.searchNotices(search, pageable));
    }
}
