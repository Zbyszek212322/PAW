package com.paw.trello.controller;

import com.paw.trello.dao.UserRepository;
import com.paw.trello.dto.TableListDto;
import com.paw.trello.entity.TableList;
import com.paw.trello.entity.User;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.AuthService;
import com.paw.trello.service.TableListService;
import com.paw.trello.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLOutput;

@RestController
@RequestMapping("/api/table-list")
public class TableListController {

    private TableListService tableListService;
    private AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public TableListController(TableListService tableListService, AuthService authService, UserRepository userRepository) {
        this.tableListService = tableListService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public Iterable<TableListDto> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return tableListService.findAll(auth);
    }

    @GetMapping("/get/{id}")
    public TableListDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return tableListService.findById(id);
    }

    @PutMapping("/{id}/{name}")
    public ResponseEntity<String> updateById(@PathVariable @RequestBody Long id, @PathVariable @RequestBody String name) throws TableNotFoundException {
        tableListService.updateById(id, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add/{tablename}")
    public ResponseEntity<TableList> addTableList(@PathVariable @RequestBody String tablename) throws TableNotFoundException {
        TableList table = new TableList();
        table.setTableName(tablename);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        table.setUser(userRepository.findByUsername(auth.getName()));
        tableListService.save(table);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

   @DeleteMapping("/delete/{id}")
   public ResponseEntity<String> deleteTableList(@PathVariable  @RequestBody Long id) {
        tableListService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TableList> updateTableList(@RequestBody TableList tableList) {
        TableList table = tableListService.save(tableList);
        return  new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PostMapping("/background/{id}")
    public ResponseEntity<String> uploadBackground(@PathVariable  @RequestBody Long id, @RequestParam("file") MultipartFile file) throws TableNotFoundException, IOException {
        tableListService.uploadBackgroundPicture(file, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/background")
    public ResponseEntity<String> deleteBackground(@RequestParam(name = "tableId") Long tableId) throws TableNotFoundException {
        tableListService.deleteBackgroundPicture(tableId);
        return  new ResponseEntity<>("Background picture for table: " + tableId + " deleted successfully", HttpStatus.OK);
    }
}
