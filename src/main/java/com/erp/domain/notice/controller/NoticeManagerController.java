package com.erp.domain.notice.controller;

import com.erp.domain.notice.dto.request.NoticeCreateDto;
import com.erp.domain.notice.service.NoticeService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
@RequestMapping("/api/manager/notices")
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
}
