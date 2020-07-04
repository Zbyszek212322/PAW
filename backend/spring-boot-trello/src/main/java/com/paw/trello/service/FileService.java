package com.paw.trello.service;

import com.paw.trello.dao.CardRepository;
import com.paw.trello.dao.FileRepository;
import com.paw.trello.dto.FileDto;
import com.paw.trello.entity.FileModel;
import com.paw.trello.entity.TableList;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final CardRepository cardRepository;

    @Autowired
    public FileService(FileRepository fileRepository, CardRepository cardRepository) {
        super();
        this.fileRepository = fileRepository;
        this.cardRepository = cardRepository;
    }

    public List <FileDto> getFiles(Long id) {
        List<FileModel> files = fileRepository.findAllByCard_Id(id);
        return files.stream().map(this::mapFromFileToDto).collect(Collectors.toList());
    }

    public List <FileDto> getAllFiles() {
        List<FileModel> files = fileRepository.findAll();
        return files.stream().map(this::mapFromFileToDto).collect(Collectors.toList());
    }

    public String uploadMultipartFile(MultipartFile file, Long cardId) {

        try {
            FileModel filemodel = new FileModel(file.getOriginalFilename(), file.getContentType(),
                    file.getBytes(), cardRepository.getOne(cardId));
            fileRepository.save(filemodel);
            return "File uploaded successfully! Filename " + file.getOriginalFilename() + " card " + cardId;
        } catch (Exception e) {
            return "FAIL!";
        }
    }

//    public TableList uploadBackgroundPicture(MultipartFile file, Long tableId) throws TableNotFoundException, IOException {
//        TableList tableList = tableListRepository.findById(tableId)
//                .orElseThrow(() -> new TableNotFoundException("Brak tabeli " + tableId));
//        tableList.setMimetype(file.getContentType());
//        tableList.setPic(compressBytes(file.getBytes()));
//        return tableListRepository.save(tableList);
//    }

    public void deleteById(Long id) {
        fileRepository.deleteById(id);
    }

    public FileDto mapFromFileToDto(FileModel fileModel) {
        FileDto fileDto = new FileDto();
        fileDto.setId(fileModel.getId());
        fileDto.setName(fileModel.getName());
        fileDto.setMimetype(fileModel.getMimetype());
        fileDto.setPic(fileModel.getPic());
        fileDto.setCard(CardService.mapFromTableListToDto(fileModel.getCard()));
        return fileDto;
    }
}
