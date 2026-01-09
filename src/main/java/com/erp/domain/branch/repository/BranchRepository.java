package com.erp.domain.branch.repository;

import com.erp.domain.branch.dto.response.BranchNameList;
import com.erp.domain.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("""
            select b.id as branchId, b.name as branchName
                        from Branch b
            """)
    List<BranchNameList> findAllBranchName();
}
