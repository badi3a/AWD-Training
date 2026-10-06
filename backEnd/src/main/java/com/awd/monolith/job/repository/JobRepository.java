package com.awd.monolith.job.repository;

import com.awd.monolith.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /** Optional filters: a null parameter means "no filter". Category is fetched to avoid N+1 queries. */
    @Query("""
            select j from Job j join fetch j.category c
            where (:available is null or j.available = :available)
              and (:categoryId is null or c.id = :categoryId)
            order by j.date desc, j.id desc
            """)
    List<Job> search(@Param("available") Boolean available, @Param("categoryId") Long categoryId);

    boolean existsByCategoryId(Long categoryId);
}
