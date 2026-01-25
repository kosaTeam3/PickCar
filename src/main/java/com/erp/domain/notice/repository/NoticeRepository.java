package com.erp.domain.notice.repository;

import com.erp.domain.notice.dto.response.NoticeSummaryDto;
import com.erp.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
                select new com.erp.domain.notice.dto.response.NoticeSummaryDto(
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
    Page<NoticeSummaryDto> searchSummaries(
            @Param("title") String title,
            @Param("writer") String writer,
            @Param("active") Boolean active,
            Pageable pageable
    );

    @Query("""
                select new com.erp.domain.notice.dto.response.NoticeSummaryDto(
                    n.id, n.employeeName, n.title, n.summary, n.active, n.pinned,
                    n.startDate, n.endDate, n.createdAt
                )
                from Notice n
                where
                    (:keyword is null or :keyword = '' or n.title like concat('%', :keyword, '%'))
                    and (
                        n.endDate is null
                        or n.active = true
                        or (n.startDate is not null and (n.endDate is null or n.endDate >= current_date))
                    )
                order by
                    case when n.pinned = true then 0 else 1 end,
                    n.createdAt desc
            """)
    Page<NoticeSummaryDto> searchSummariesClient(Pageable pageable, String keyword);
}
