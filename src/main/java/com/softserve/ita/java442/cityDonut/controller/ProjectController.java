package com.softserve.ita.java442.cityDonut.controller;

import com.softserve.ita.java442.cityDonut.dto.fieldsCheck.FieldsCheckDto;
import com.softserve.ita.java442.cityDonut.dto.project.MainProjectInfoDto;
import com.softserve.ita.java442.cityDonut.dto.project.PreviewProjectDto;
import com.softserve.ita.java442.cityDonut.service.FieldsCheckService;
import com.softserve.ita.java442.cityDonut.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FieldsCheckService fieldsCheckService;

    @GetMapping("/project/{id}")
    public ResponseEntity<MainProjectInfoDto> getProjectById(@PathVariable long id) {
        return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
    }

    @GetMapping("/project/filter")
    public ResponseEntity<List<PreviewProjectDto>> filter(
            @RequestParam List<String> categories, long moneyFrom, long moneyTo, String status) {
        return new ResponseEntity<>(
                projectService.getFilteredProjects(categories, moneyFrom, moneyTo, status), HttpStatus.OK);
    }

    @GetMapping("/project/{id}/fields/valid")
    public ResponseEntity<List<FieldsCheckDto>> getFieldsValidationInfo(@PathVariable long id) {
        return new ResponseEntity<>(fieldsCheckService.getAllByProjectId(id), HttpStatus.OK);
    }

    @PutMapping("/project/field/valid/change")
    public ResponseEntity<FieldsCheckDto> updateFieldCheck(@RequestBody FieldsCheckDto fieldsCheckDto) {
        return new ResponseEntity<>(fieldsCheckService.update(fieldsCheckDto), HttpStatus.OK);
    }
}
