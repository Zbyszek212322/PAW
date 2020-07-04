package com.paw.trello.controller;

import com.paw.trello.dto.FileDto;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public List<FileDto> getFiles(@RequestParam("cardId") String cardId) {
        return fileService.getFiles(Long.parseLong(cardId));
    }

    @GetMapping("/all")
    public List<FileDto> getAllFiles() {
        return fileService.getAllFiles();
    }

    @PostMapping("/uploadAttachment/{cardId}")
    public ResponseEntity<String> uploadMultipartFile2(@PathVariable  @RequestBody Long cardId, @RequestParam("file") MultipartFile file) {
        fileService.uploadMultipartFile(file, cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam(name = "id") Long fileId) {
        fileService.deleteById(fileId);
        return new ResponseEntity<>("File with ID:" + fileId + " deleted successfully", HttpStatus.OK);
    }
}
