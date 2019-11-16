package com.softserve.ita.java442.cityDonut.service.impl;

import com.softserve.ita.java442.cityDonut.constant.ErrorMessage;
import com.softserve.ita.java442.cityDonut.dto.media.FileStorageProperties;
import com.softserve.ita.java442.cityDonut.dto.media.MediaDto;
import com.softserve.ita.java442.cityDonut.dto.media.MediaTypeDto;
import com.softserve.ita.java442.cityDonut.exception.FileStorageException;
import com.softserve.ita.java442.cityDonut.mapper.media.MediaMapper;
import com.softserve.ita.java442.cityDonut.model.Media;
import com.softserve.ita.java442.cityDonut.model.MediaType;
import com.softserve.ita.java442.cityDonut.repository.MediaRepository;
import com.softserve.ita.java442.cityDonut.repository.MediaTypeRepository;
import com.softserve.ita.java442.cityDonut.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    FileStorageServiceImpl fileStorage;

    @Autowired
    MediaServiceImpl mediaService;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MediaMapper mediaMapper;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {

        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(ErrorMessage.FILE_NOT_FOUND);
        }
    }

    @Override
    public String storeFile(MultipartFile file, long projectId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        MediaDto mediaDto = new MediaDto();
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException(ErrorMessage.INVALID_CHARACTER + fileName);
            }
            mediaDto.setProjectId(projectId);
            mediaService.saveMedia(mediaDto, fileName);
            String FileIdWithExt = mediaService.fileIDWithExtension(mediaDto);
            Path targetLocation = this.fileStorageLocation.resolve(FileIdWithExt);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException(fileName + ErrorMessage.COULD_NOT_STORE_FILE);
        }
    }

    public Resource loadFileAsResource(String fileName, long projectId) {
        MediaDto mediaDto = mediaMapper.convertToDto(mediaService.getFileByNameAndProjectId(fileName, projectId));
        try {
            String FileIdWithExt = mediaService.fileIDWithExtension(mediaDto);
            Path filePath = this.fileStorageLocation.resolve(FileIdWithExt).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException(ErrorMessage.FILE_NOT_FOUND + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException(ErrorMessage.FILE_NOT_FOUND + fileName);
        }
    }

    public List<String> getPhotoNames(long projectId) {
        List<MediaDto> mediaDtoList = mediaService.getPhotoNames(projectId);
//        ArrayList<String> fileNames= new ArrayList<>();
//        for (MediaDto dto : mediaDtoList) {
//            fileNames.add(dto.getName());
//        }
        return getNames(mediaDtoList);
     //   return buildUrls(projectId, mediaDtoList);
    }

    public List<String> getFileNames(long projectId) {
        List<MediaDto> mediaDtoList = mediaService.getFileNames(projectId);
        return getNames(mediaDtoList);
       // return buildUrls(projectId,mediaDtoList);
    }

    public String getAvatarName(long projectId) {
        ArrayList<MediaDto> listOfDto = (ArrayList<MediaDto>) mediaService.getPhotoNames(projectId);
        MediaDto dto = listOfDto.get(0);
        return dto.getName();
    }

    public boolean delete(long projectId, String fileName) {
        MediaDto mediaDto = mediaMapper.convertToDto(mediaService.getFileByNameAndProjectId(fileName, projectId));
        String FileIdWithExt = mediaService.fileIDWithExtension(mediaDto);
        Path filePath = this.fileStorageLocation.resolve(FileIdWithExt).normalize();
        File file = new File(String.valueOf(filePath));
        if (file.delete()) {
            mediaService.deleteInDB(mediaDto);
            return true;
        } else {
            throw new FileStorageException(ErrorMessage.FILE_NOT_FOUND + fileName);
        }
    }

    private ArrayList<String> getNames(List<MediaDto> mediaDtoList){
        ArrayList<String> fileNames= new ArrayList<>();
        for (MediaDto dto : mediaDtoList) {
            fileNames.add(dto.getName());
        }
        return fileNames;
    }

//    private List<String> buildUrls(long projectId, List<MediaDto> mediaDtoList) {
//        ArrayList<String> result = new ArrayList<>();
//        String url = "http://localhost:8091/api/v1/project/";
//        String nameOfFunction = "/downloadFile/";
//        for (MediaDto dto : mediaDtoList) {
//            result.add(url + projectId + nameOfFunction + dto.getName());
//        }
//        return result;
//    }

}
