package com.softserve.ita.java442.cityDonut.service.impl;

import com.softserve.ita.java442.cityDonut.dto.projectStatus.ProjectStatusDto;
import com.softserve.ita.java442.cityDonut.mapper.projectStatus.ProjectStatusMapper;
import com.softserve.ita.java442.cityDonut.model.ProjectStatus;
import com.softserve.ita.java442.cityDonut.repository.ProjectStatusRepository;
import com.softserve.ita.java442.cityDonut.service.ProjectStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectStatusServiceImpl implements ProjectStatusService {

    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    @Autowired
    private ProjectStatusMapper projectStatusMapper;

    @Override
    public ProjectStatus getById(Long id) {
        return projectStatusRepository.getOne(id);
    }

    @Override
    public List<ProjectStatusDto> getStatusesAfterValidation() {
        List<String> notNeededStatuses = new ArrayList<>();
        notNeededStatuses.add("чернетка");
        notNeededStatuses.add("очікує підтвердження");
        notNeededStatuses.add("на перевірці");
        return projectStatusMapper.convertListToDto(projectStatusRepository.getByStatusIsNotIn(notNeededStatuses));
    }

    @Override
    public List<ProjectStatusDto> getAllStatuses() {
        return projectStatusMapper.convertListToDto(projectStatusRepository.findAll());
    }
}
