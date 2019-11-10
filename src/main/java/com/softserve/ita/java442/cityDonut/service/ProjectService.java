package com.softserve.ita.java442.cityDonut.service;

import com.softserve.ita.java442.cityDonut.dto.project.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    MainProjectInfoDto getProjectById(long id);

    NewProjectDto saveProject(NewProjectDto project, long userId);

    EditedProjectDto editProject(EditedProjectDto project, long projectId, long userId);

    Long getMaxMoneyNeeded();

    List<PreviewProjectDto> getFilteredProjects(List<String> categoryIds, String statusId, long moneyFrom, String moneyTo, Pageable pageable);

    List<ProjectByUserDonateDto> getDonatedUserProject(long id, Pageable pageable);

    List<ProjectInfoDto> getFreeProject();

    MainProjectInfoDto addModeratorToProject(long projectId);

}
