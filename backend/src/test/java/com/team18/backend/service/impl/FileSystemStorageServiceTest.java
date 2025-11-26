package com.team18.backend.service.impl;

import com.team18.backend.exception.StorageException;
import com.team18.backend.model.enums.OwnerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileSystemStorageServiceTest {

    private FileSystemStorageService storageService;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        tempDir = Files.createTempDirectory("storage_test_");

        storageService = new FileSystemStorageService();

        var field = FileSystemStorageService.class.getDeclaredField("storageLocation");
        field.setAccessible(true);
        field.set(storageService, tempDir.toString());

        storageService.init();
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(tempDir);
    }

    @Test
    void init_shouldCreateDirectoriesForAllOwnerTypes() {
        for (OwnerType type : OwnerType.values()) {
            Path dir = tempDir.resolve(type.toString().toLowerCase());
            assertTrue(Files.exists(dir));
            assertTrue(Files.isDirectory(dir));
        }
    }

    @Test
    void init_shouldThrowStorageExceptionOnIOException() throws NoSuchFieldException, IllegalAccessException {
        FileSystemStorageService service = new FileSystemStorageService();

        Field field = FileSystemStorageService.class.getDeclaredField("storageLocation");
        field.setAccessible(true);
        field.set(service, "/some/path");

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(Mockito.any(Path.class)))
                    .thenThrow(new IOException("Simulated IO error"));

            assertThrows(StorageException.class, service::init);
        }
    }

    @Test
    void store_shouldStoreFileCorrectly() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("image.jpg");
        when(mockFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("testcontent".getBytes()));

        String savedName = storageService.store(mockFile, OwnerType.ITEM, "avatar");

        assertEquals("avatar.jpg", savedName);

        Path savedFile = tempDir
                .resolve("item")
                .resolve("avatar.jpg");

        assertTrue(Files.exists(savedFile));
        assertEquals("testcontent", Files.readString(savedFile));
    }

    @Test
    void store_shouldThrowException_WhenFileIsEmpty() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        assertThrows(StorageException.class, () ->
                storageService.store(mockFile, OwnerType.ITEM, "avatar"));
    }

    @Test
    void store_shouldThrowExceptionOnIOFailure() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("photo.png");
        when(mockFile.getInputStream()).thenThrow(new IOException("IO error"));

        assertThrows(StorageException.class, () ->
                storageService.store(mockFile, OwnerType.ITEM, "avatar"));
    }

    @Test
    void store_shouldSanitizeDangerousFilename() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("evil.txt");
        when(mockFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("malicious".getBytes()));

        String dangerousFilename = "../../evil_file";
        String savedName = storageService.store(mockFile, OwnerType.ITEM, dangerousFilename);

        assertFalse(savedName.contains(".."));
        assertFalse(savedName.contains("/"));
        assertFalse(savedName.contains("\\"));

        Path savedFile = Paths.get(tempDir.toString(), "item", savedName);
        assertTrue(Files.exists(savedFile));
    }


    @Test
    void loadAsResource_shouldReturnResourceForExistingFile() throws IOException {
        Path filePath = tempDir.resolve("item/testfile.txt");
        Files.writeString(filePath, "filecontent");

        Optional<Resource> resourceOpt = storageService.loadAsResource("testfile.txt", OwnerType.ITEM);

        assertTrue(resourceOpt.isPresent());
        Resource resource = resourceOpt.get();
        assertTrue(resource.exists());

        try (var is = resource.getInputStream()) {
            assertEquals("filecontent", new String(is.readAllBytes()));
        }
    }

    @Test
    void loadAsResource_shouldReturnEmptyOptionalForNonExistingFile() {
        Optional<Resource> resourceOpt = storageService.loadAsResource("nonexistent.txt", OwnerType.ITEM);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    void loadAsResource_shouldReturnEmptyOptionalForMalformedURL() {
        try (MockedStatic<Paths> mockedPaths = Mockito.mockStatic(Paths.class)) {
            mockedPaths.when(() -> Paths.get(Mockito.anyString()))
                    .thenReturn(Path.of("valid_path"));

            try (MockedStatic<Path> mockedPath = Mockito.mockStatic(Path.class)) {
                Path mockPath = mock(Path.class);
                when(mockPath.resolve(Mockito.anyString())).thenReturn(mockPath);
                when(mockPath.toUri()).thenThrow(new RuntimeException("Test") {
                    @Override
                    public synchronized Throwable getCause() {
                        return new MalformedURLException("Simulated malformed URL");
                    }
                });

                mockedPath.when(() -> Path.of(Mockito.anyString())).thenReturn(mockPath);

                Optional<Resource> resourceOpt = storageService.loadAsResource("file.txt", OwnerType.ITEM);
                assertTrue(resourceOpt.isEmpty());
            }
        }
    }

    @Test
    void loadAsResource_shouldReturnResource_WhenOnlyReadableIsTrue() {
        try (MockedConstruction<UrlResource> mocked = Mockito.mockConstruction(
                UrlResource.class,
                (mock, context) -> {
                    when(mock.exists()).thenReturn(false);
                    when(mock.isReadable()).thenReturn(true); // важная часть
                }
        )) {

            Optional<Resource> result = storageService.loadAsResource("test.txt", OwnerType.ITEM);

            assertTrue(result.isPresent());
        }
    }

    @Test
    void loadAsResource_shouldReturnEmptyOptional_WhenResourceNotReadable() {
        try (MockedConstruction<UrlResource> mocked = Mockito.mockConstruction(
                UrlResource.class,
                (mock, context) -> {
                    when(mock.exists()).thenReturn(false);
                    when(mock.isReadable()).thenReturn(false);
                }
        )) {

            Optional<Resource> result = storageService.loadAsResource("any.txt", OwnerType.ITEM);

            assertTrue(result.isEmpty());
        }
    }

}
