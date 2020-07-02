package com.paw.trello.service;

import com.paw.trello.dao.TableListRepository;
import com.paw.trello.dao.UserRepository;
import com.paw.trello.dto.TableListDto;
import com.paw.trello.entity.TableList;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.util.stream.Collectors.toList;

@Service
public class TableListService {

    private final TableListRepository tableListRepository;
    private final UserRepository userRepository;

    @Autowired
    public TableListService(TableListRepository tableListRepository, UserRepository userRepository) {
        super();
        this.tableListRepository = tableListRepository;
        this.userRepository = userRepository;
    }

    public TableListDto findById(Long id) throws TableNotFoundException {
        TableList tableList = tableListRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromTableListToDto(tableList);
    }

    public Iterable<TableListDto> findAll(Authentication auth) {
        Set<TableList> tableLists = tableListRepository.getAllByUserId(userRepository.findByUsername(auth.getName()).getId());
        return tableLists.stream().map(this::mapFromTableListToDto).collect(toList());
    }

    public TableList save(TableList tableList) {
        return tableListRepository.save(tableList);
    }

    public void deleteById(Long id) {
        tableListRepository.deleteById(id);
    }

    public TableListDto mapFromTableListToDto(TableList tableList) {
        TableListDto tableListDto = new TableListDto();
        tableListDto.setId(tableList.getId());
        tableListDto.setTableName(tableList.getTableName());
        tableListDto.setUser(tableList.getUser().getUsername());
        tableListDto.setMimetype(tableList.getMimetype());
        tableListDto.setPic(decompressBytes(tableList.getPic()));
        return tableListDto;
    }

    public void updateById(Long id, String name) throws TableNotFoundException {
        TableList tableList = tableListRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        tableList.setTableName(name);
        tableListRepository.save(tableList);
    }

    public TableList uploadBackgroundPicture(MultipartFile file, Long tableId) throws TableNotFoundException, IOException {
        TableList tableList = tableListRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException("Brak tabeli " + tableId));
        tableList.setMimetype(file.getContentType());
        tableList.setPic(compressBytes(file.getBytes()));
        return tableListRepository.save(tableList);
    }

    public void deleteBackgroundPicture(Long tableId) throws TableNotFoundException{

        TableList tableList = tableListRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException("Brak tabeli " + tableId));
        tableList.setMimetype(null);
        tableList.setPic(null);
        tableListRepository.save(tableList);
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        if(data != null) {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] buffer = new byte[1024];
            try {
                while (!inflater.finished()) {
                    int count = inflater.inflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
            } catch (IOException ioe) {
            } catch (DataFormatException e) {
                e.printStackTrace();
            }

            return outputStream.toByteArray();
        } else {
            return data;
        }
    }
}
