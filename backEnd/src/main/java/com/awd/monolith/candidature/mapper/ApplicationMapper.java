package com.awd.monolith.candidature.mapper;

import com.awd.monolith.candidat.entity.Candidate;
import com.awd.monolith.candidature.dto.ApplicationResponse;
import com.awd.monolith.candidature.entity.Application;
import com.awd.monolith.job.entity.Job;

/** Converts Application entities to DTOs. */
public final class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static ApplicationResponse toResponse(Application application) {
        Candidate c = application.getCandidate();
        Job j = application.getJob();
        return new ApplicationResponse(
                application.getId(),
                application.getApplicationDate(),
                application.getMotivation(),
                new ApplicationResponse.CandidateSummary(c.getId(), c.getFirstname(), c.getLastname(), c.getEmail()),
                new ApplicationResponse.JobSummary(j.getId(), j.getName(), j.isAvailable(), j.getCategory().getName()));
    }
}
