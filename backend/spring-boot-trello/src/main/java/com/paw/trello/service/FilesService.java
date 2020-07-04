package com.paw.trello.service;

import com.paw.trello.dao.CardsRepository;
import com.paw.trello.dao.FilesRepository;
import com.paw.trello.dto.FilesDto;
import com.paw.trello.entity.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilesService {

    private final FilesRepository filesRepository;
    private final CardsRepository cardsRepository;

    @Autowired
    public FilesService(FilesRepository filesRepository, CardsRepository cardsRepository) {
        super();
        this.filesRepository = filesRepository;
        this.cardsRepository = cardsRepository;
    }

    public List <FilesDto> getFiles(Long id) {
        List<Files> files = filesRepository.findAllByCards_Id(id);
        return files.stream().map(this::mapFromFileToDto).collect(Collectors.toList());
    }

    public List <FilesDto> getAllFiles() {
        List<Files> files = filesRepository.findAll();
        return files.stream().map(this::mapFromFileToDto).collect(Collectors.toList());
    }

    public String uploadMultipartFile(MultipartFile file, Long cardId) {

        try {
            Files filemodel = new Files(file.getOriginalFilename(), file.getContentType(),
                    file.getBytes(), cardsRepository.getOne(cardId));
            filesRepository.save(filemodel);
            return "File uploaded successfully! Filename " + file.getOriginalFilename() + " card " + cardId;
        } catch (Exception e) {
            return "FAIL!";
        }
    }

    public void deleteById(Long id) {
        filesRepository.deleteById(id);
    }

    public FilesDto mapFromFileToDto(Files files) {
        FilesDto filesDto = new FilesDto();
        filesDto.setId(files.getId());
        filesDto.setName(files.getName());
        filesDto.setMimetype(files.getMimetype());
        filesDto.setPic(files.getPic());
        filesDto.setCard(CardsService.mapFromTableListToDto(files.getCards()));
        return filesDto;
    }
}
