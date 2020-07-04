package com.paw.trello.controller;

import com.paw.trello.dto.FilesDto;
import com.paw.trello.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private FilesService filesService;

    @Autowired
    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping
    public List<FilesDto> getFiles(@RequestParam("cardId") String cardId) {
        return filesService.getFiles(Long.parseLong(cardId));
    }

    @GetMapping("/all")
    public List<FilesDto> getAllFiles() {
        return filesService.getAllFiles();
    }

    @PostMapping("/uploadAttachment/{cardId}")
    public ResponseEntity<String> uploadMultipartFile2(@PathVariable  @RequestBody Long cardId, @RequestParam("file") MultipartFile file) {
        filesService.uploadMultipartFile(file, cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam(name = "id") Long fileId) {
        filesService.deleteById(fileId);
        return new ResponseEntity<>("File with ID:" + fileId + " deleted successfully", HttpStatus.OK);
    }
}
