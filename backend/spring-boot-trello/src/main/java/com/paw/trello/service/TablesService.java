package com.paw.trello.service;

import com.paw.trello.dao.TablesRepository;
import com.paw.trello.dao.UsersRepository;
import com.paw.trello.dto.TablesDto;
import com.paw.trello.entity.Tables;
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
public class TablesService {

    private final TablesRepository tablesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public TablesService(TablesRepository tablesRepository, UsersRepository usersRepository) {
        super();
        this.tablesRepository = tablesRepository;
        this.usersRepository = usersRepository;
    }

    public TablesDto findById(Long id) throws TableNotFoundException {
        Tables tables = tablesRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromTableListToDto(tables);
    }

    public Iterable<TablesDto> findAll(Authentication auth) {
        Set<Tables> tables = tablesRepository.getAllByUsersId(usersRepository.findByUsername(auth.getName()).getId());
        return tables.stream().map(this::mapFromTableListToDto).collect(toList());
    }

    public Tables save(Tables tables) {
        return tablesRepository.save(tables);
    }

    public void deleteById(Long id) {
        tablesRepository.deleteById(id);
    }

    public TablesDto mapFromTableListToDto(Tables tables) {
        TablesDto tablesDto = new TablesDto();
        tablesDto.setId(tables.getId());
        tablesDto.setTableName(tables.getTableName());
        tablesDto.setUser(tables.getUsers().getUsername());
        tablesDto.setMimetype(tables.getMimetype());
        tablesDto.setPic(decompressBytes(tables.getPic()));
        return tablesDto;
    }

    public void updateById(Long id, String name) throws TableNotFoundException {
        Tables tables = tablesRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        tables.setTableName(name);
        tablesRepository.save(tables);
    }

    public Tables uploadBackgroundPicture(MultipartFile file, Long tableId) throws TableNotFoundException, IOException {
        Tables tables = tablesRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException("Brak tabeli " + tableId));
        tables.setMimetype(file.getContentType());
        tables.setPic(compressBytes(file.getBytes()));
        return tablesRepository.save(tables);
    }

    public void deleteBackgroundPicture(Long tableId) throws TableNotFoundException{

        Tables tables = tablesRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException("Brak tabeli " + tableId));
        tables.setMimetype(null);
        tables.setPic(null);
        tablesRepository.save(tables);
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
