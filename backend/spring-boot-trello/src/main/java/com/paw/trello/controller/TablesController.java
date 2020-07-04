package com.paw.trello.controller;

import com.paw.trello.dao.UsersRepository;
import com.paw.trello.dto.TablesDto;
import com.paw.trello.entity.Tables;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.AuthService;
import com.paw.trello.service.TablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/table-list")
public class TablesController {

    private TablesService tablesService;
    private AuthService authService;
    private final UsersRepository usersRepository;

    @Autowired
    public TablesController(TablesService tablesService, AuthService authService, UsersRepository usersRepository) {
        this.tablesService = tablesService;
        this.authService = authService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/all")
    public Iterable<TablesDto> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return tablesService.findAll(auth);
    }

    @GetMapping("/get/{id}")
    public TablesDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return tablesService.findById(id);
    }

    @PutMapping("/{id}/{name}")
    public ResponseEntity<String> updateById(@PathVariable @RequestBody Long id, @PathVariable @RequestBody String name) throws TableNotFoundException {
        tablesService.updateById(id, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add/{tablename}")
    public ResponseEntity<Tables> addTableList(@PathVariable @RequestBody String tablename) throws TableNotFoundException {
        Tables table = new Tables();
        table.setTableName(tablename);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        table.setUsers(usersRepository.findByUsername(auth.getName()));
        tablesService.save(table);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

   @DeleteMapping("/delete/{id}")
   public ResponseEntity<String> deleteTableList(@PathVariable  @RequestBody Long id) {
        tablesService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Tables> updateTableList(@RequestBody Tables tables) {
        Tables table = tablesService.save(tables);
        return  new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PostMapping("/background/{id}")
    public ResponseEntity<String> uploadBackground(@PathVariable  @RequestBody Long id, @RequestParam("file") MultipartFile file) throws TableNotFoundException, IOException {
        tablesService.uploadBackgroundPicture(file, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/background")
    public ResponseEntity<String> deleteBackground(@RequestParam(name = "tableId") Long tableId) throws TableNotFoundException {
        tablesService.deleteBackgroundPicture(tableId);
        return  new ResponseEntity<>("Background picture for table: " + tableId + " deleted successfully", HttpStatus.OK);
    }
}
