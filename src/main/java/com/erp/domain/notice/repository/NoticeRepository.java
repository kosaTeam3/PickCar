package com.erp.domain.notice.repository;

import com.erp.domain.notice.dto.NoticeSummaryResponse;
import com.erp.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
                select new com.erp.domain.notice.dto.NoticeSummaryResponse(
                    n.id, n.employeeName, n.title, n.summary, n.active, n.pinned,
                    n.startDate, n.endDate, n.createdAt
                )
                from Notice n
                where (:active is null or n.active = :active)
                  and (
                        (:title is null and :writer is null)
                        or (:title is not null and n.title like concat('%', :title, '%'))
                        or (:writer is not null and n.employeeName like concat('%', :writer, '%'))
                      )
            """)
    Page<NoticeSummaryResponse> searchSummaries(
            @Param("title") String title,
            @Param("writer") String writer,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
