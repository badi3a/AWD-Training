package com.awd.job.service;

import com.awd.job.dto.JobRequest;
import com.awd.job.dto.JobResponse;
import com.awd.job.entity.Job;
import com.awd.job.exception.ResourceNotFoundException;
import com.awd.job.mapper.JobMapper;
import com.awd.job.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final CategoryService categoryService;

    public JobService(JobRepository jobRepository, CategoryService categoryService) {
        this.jobRepository = jobRepository;
        this.categoryService = categoryService;
    }

    /** Lists jobs, optionally filtered by availability and/or category. */
    @Transactional(readOnly = true)
    public List<JobResponse> search(Boolean available, Long categoryId) {
        if (categoryId != null) {
            categoryService.getCategory(categoryId); // 404 if the category does not exist
        }
        return jobRepository.search(available, categoryId).stream().map(JobMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public JobResponse findById(Long id) {
        return JobMapper.toResponse(getJob(id));
    }

    public JobResponse create(JobRequest request) {
        Job job = new Job();
        JobMapper.updateEntity(job, request, categoryService.getCategory(request.categoryId()));
        return JobMapper.toResponse(jobRepository.save(job));
    }

    public JobResponse update(Long id, JobRequest request) {
        Job job = getJob(id);
        JobMapper.updateEntity(job, request, categoryService.getCategory(request.categoryId()));
        return JobMapper.toResponse(job);
    }

    /** Opens or closes a job without sending the whole object. */
    public JobResponse setAvailability(Long id, boolean available) {
        Job job = getJob(id);
        job.setAvailable(available);
        return JobMapper.toResponse(job);
    }

    public void delete(Long id) {
        jobRepository.delete(getJob(id));
    }

    private Job getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", id));
    }
}
