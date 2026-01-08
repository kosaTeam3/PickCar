package com.erp.domain.branch.repository;

import com.erp.domain.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query(value = "SELECT b.*, " +
            "(ST_Distance_Sphere(point(b.longitude, b.latitude), point(:userLongitude, :userLatitude)) / 1000) AS distance " +
            "FROM branch b " +
            "ORDER BY distance", nativeQuery = true)
    List<BranchWithDistance> findBranchesByDistance(@Param("userLatitude") Double userLatitude,
                                                    @Param("userLongitude") Double userLongitude);
}
