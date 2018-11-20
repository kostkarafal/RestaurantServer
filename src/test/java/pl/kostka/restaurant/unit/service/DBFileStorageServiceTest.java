package pl.kostka.restaurant.unit.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import pl.kostka.restaurant.exception.FileStorageException;
import pl.kostka.restaurant.exception.MyFileNotFoundException;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.repository.DBFileRepository;
import pl.kostka.restaurant.service.DBFileStorageService;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)

public class DBFileStorageServiceTest {

    @MockBean
    private DBFileRepository dbFileRepository;

    private DBFileStorageService dbFileStorageService;

    @Before
    public void init(){
        dbFileStorageService = new DBFileStorageService(dbFileRepository);
    }

    @Test(expected = FileStorageException.class)
    public void testStoreFile_WrongFileName(){
        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("wrong..file...name");

        dbFileStorageService.storeFile(file);
    }

    @Test(expected = FileStorageException.class)
    public void testUpdateFile_WrongFileName(){
        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("wrong..file...name");

        dbFileStorageService.updateFile(file,1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateFile_NotFoundInDatabase(){
        MultipartFile file = Mockito.mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("test.jpg");

        dbFileStorageService.updateFile(file,1L);
    }

    @Test(expected = MyFileNotFoundException.class)
    public void testGetFile_FileNotFound(){

        dbFileStorageService.getFile(1L);
    }

}
