package com.softserve.ita.java442.cityDonut.service.impl;

import com.softserve.ita.java442.cityDonut.dto.category.CategoryDto;
import com.softserve.ita.java442.cityDonut.mapper.category.CategoryMapper;
import com.softserve.ita.java442.cityDonut.model.Category;
import com.softserve.ita.java442.cityDonut.repository.CategoryRepository;
import com.softserve.ita.java442.cityDonut.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoriesByIds(List<Long> categoryIds) {
        List<Category> requiredCategories = new ArrayList<>();
        categoryIds.forEach((categoryId) ->
                requiredCategories.add(categoryRepository.getOne(categoryId)));
        return requiredCategories;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.convertListToDto(categoryRepository.findAll());
    }
}
