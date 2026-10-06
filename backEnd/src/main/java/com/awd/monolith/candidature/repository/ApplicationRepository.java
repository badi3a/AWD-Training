package com.awd.monolith.candidature.repository;

import com.awd.monolith.candidature.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /** Optional filters: a null parameter means "no filter". Candidate and job are fetched in the same query. */
    @Query("""
            select a from Application a
            join fetch a.candidate c
            join fetch a.job j
            join fetch j.category
            where (:candidateId is null or c.id = :candidateId)
              and (:jobId is null or j.id = :jobId)
            order by a.applicationDate desc, a.id desc
            """)
    List<Application> search(@Param("candidateId") Long candidateId, @Param("jobId") Long jobId);

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    boolean existsByCandidateId(Long candidateId);

    boolean existsByJobId(Long jobId);
}
