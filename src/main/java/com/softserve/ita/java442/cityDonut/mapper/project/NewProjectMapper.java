package com.softserve.ita.java442.cityDonut.mapper.project;

import com.softserve.ita.java442.cityDonut.dto.project.NewProjectDTO;
import com.softserve.ita.java442.cityDonut.mapper.GeneralMapper;
import com.softserve.ita.java442.cityDonut.mapper.category.CategoryNameMapper;
import com.softserve.ita.java442.cityDonut.mapper.status.ProjectStatusMapper;
import com.softserve.ita.java442.cityDonut.mapper.user.UserMapper;
import com.softserve.ita.java442.cityDonut.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewProjectMapper implements GeneralMapper<Project, NewProjectDTO> {

    @Autowired
    CategoryNameMapper categoryNameMapper;

    @Override
    public NewProjectDTO convertToDto(Project project) {
        return NewProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .location(project.getLocation())
                .locationLatitude(project.getLocationLatitude())
                .locationLongitude(project.getLocationLongitude())
                .categories(categoryNameMapper.convertListToDto(project.getCategories()))
                .build();
    }

    @Override
    public Project convertToModel(NewProjectDTO projectDTO) {
        return Project.builder()
                .id(projectDTO.getId())
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .location(projectDTO.getLocation())
                .locationLatitude(projectDTO.getLocationLatitude())
                .locationLongitude(projectDTO.getLocationLongitude())
                .categories(categoryNameMapper.convertListToModel(projectDTO.getCategories()))
                .build();
    }

}
