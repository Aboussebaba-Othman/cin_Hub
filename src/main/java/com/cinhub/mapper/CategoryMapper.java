package com.cinhub.mapper;

import com.cinhub.dto.CategoryCreateDTO;
import com.cinhub.dto.CategoryDTO;
import com.cinhub.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .idCategory(category.getIdCategory())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public Category toEntity(CategoryCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}