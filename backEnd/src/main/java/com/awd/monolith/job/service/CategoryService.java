package com.awd.monolith.job.service;

import com.awd.monolith.job.dto.CategoryRequest;
import com.awd.monolith.job.dto.CategoryResponse;
import com.awd.monolith.job.entity.Category;
import com.awd.monolith.common.exception.ConflictException;
import com.awd.monolith.common.exception.ResourceNotFoundException;
import com.awd.monolith.job.mapper.JobMapper;
import com.awd.monolith.job.repository.CategoryRepository;
import com.awd.monolith.job.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;

    public CategoryService(CategoryRepository categoryRepository, JobRepository jobRepository) {
        this.categoryRepository = categoryRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(JobMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return JobMapper.toResponse(getCategory(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ConflictException("A category named '" + request.name() + "' already exists");
        }
        return JobMapper.toResponse(categoryRepository.save(JobMapper.toEntity(request)));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("A category named '" + request.name() + "' already exists");
        }
        JobMapper.updateEntity(category, request);
        return JobMapper.toResponse(category);
    }

    /** A category that still has jobs cannot be deleted (matches ON DELETE RESTRICT in the SQL script). */
    public void delete(Long id) {
        Category category = getCategory(id);
        if (jobRepository.existsByCategoryId(id)) {
            throw new ConflictException("Category " + id + " still has jobs; delete or move them first");
        }
        categoryRepository.delete(category);
    }

    Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
