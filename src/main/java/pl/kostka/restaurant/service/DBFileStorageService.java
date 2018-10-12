package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.kostka.restaurant.exception.FileStorageException;
import pl.kostka.restaurant.exception.MyFileNotFoundException;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.DBFile;
import pl.kostka.restaurant.repository.DBFileRepository;

import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public DBFile storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence");
            }

            DBFile dbFile = new DBFile(fileName,file.getContentType(),file.getBytes());
            return dbFileRepository.save(dbFile);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", e);
        }
    }

    public DBFile updateFile(MultipartFile file, Long fileId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence");
            }
            byte[] fileData = file.getBytes();
            return dbFileRepository.findById(fileId).map(dbFile -> {
                dbFile.setData(fileData);
                dbFile.setFileName(fileName);
                dbFile.setFileType(file.getContentType());
                return dbFileRepository.save(dbFile);
            }).orElseThrow(() -> new ResourceNotFoundException("FileId "+ fileId + " not found"));
        } catch (IOException e) {
            throw new FileStorageException("Could not store file" + fileName + ". Please try again!", e);
        }
    }

    public DBFile getFile(Long fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found id " + fileId.toString()));
    }
}
