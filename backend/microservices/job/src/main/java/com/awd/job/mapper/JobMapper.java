package com.awd.job.mapper;

import com.awd.job.dto.CategoryRequest;
import com.awd.job.dto.CategoryResponse;
import com.awd.job.dto.JobRequest;
import com.awd.job.dto.JobResponse;
import com.awd.job.entity.Category;
import com.awd.job.entity.Job;

/** Converts between entities and DTOs. */
public final class JobMapper {

    private JobMapper() {
    }

    public static Category toEntity(CategoryRequest request) {
        return new Category(request.name().trim(), request.description());
    }

    public static void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.name().trim());
        category.setDescription(request.description());
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    /** Copies the request fields onto the job. The category is set by the service. */
    public static void updateEntity(Job job, JobRequest request, Category category) {
        job.setName(request.name().trim());
        job.setDescription(request.description());
        job.setAvailable(request.available());
        job.setDate(request.date());
        job.setCategory(category);
    }

    public static JobResponse toResponse(Job job) {
        return new JobResponse(job.getId(), job.getName(), job.getDescription(), job.isAvailable(),
                job.getDate(), toResponse(job.getCategory()));
    }
}
