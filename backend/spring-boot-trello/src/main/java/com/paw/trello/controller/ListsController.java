package com.paw.trello.controller;

import com.paw.trello.dto.ListsDto;
import com.paw.trello.dto.ListsPost;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.ListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card-list")
public class ListsController {

    private ListsService listsService;

    @Autowired
    public ListsController(ListsService listsService) {
        this.listsService = listsService;
    }

    @GetMapping("/all")
    public Iterable<ListsDto> getAll() {
        return listsService.findAll();
    }

    @GetMapping("/get/{id}")
    public ListsDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return listsService.findById(id);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archive(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        listsService.archive(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/table/{id}")
    public List<ListsDto> findAllCardsFromTable(@PathVariable @RequestBody Long id) {
        return listsService.findAllCardListsFromTable(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCardList(@RequestBody ListsPost cardList) throws TableNotFoundException {
        listsService.add(cardList);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveLeft/{id}")
    public ResponseEntity<String> moveLeft(@PathVariable @RequestBody Long id) {
        listsService.moveLeft(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveRight/{id}")
    public ResponseEntity<String> moveRight(@PathVariable @RequestBody Long id) {
        listsService.moveRight(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCard(@RequestBody ListsPost cardListPost) throws TableNotFoundException {
        listsService.update(cardListPost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}

