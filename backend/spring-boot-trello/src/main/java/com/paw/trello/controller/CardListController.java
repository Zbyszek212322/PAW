package com.paw.trello.controller;

import com.paw.trello.dto.CardListDto;
import com.paw.trello.dto.CardListPost;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.CardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card-list")
public class CardListController {

    private CardListService cardListService;

    @Autowired
    public CardListController(CardListService cardListService) {
        this.cardListService = cardListService;
    }

    @GetMapping("/all")
    public Iterable<CardListDto> getAll() {
        return cardListService.findAll();
    }

    @GetMapping("/get/{id}")
    public CardListDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return cardListService.findById(id);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<String> archive(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        cardListService.archive(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/table/{id}")
    public List<CardListDto> findAllCardsFromTable(@PathVariable @RequestBody Long id) {
        return cardListService.findAllCardListsFromTable(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCardList(@RequestBody CardListPost cardList) throws TableNotFoundException {
        cardListService.add(cardList);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveLeft/{id}")
    public ResponseEntity<String> moveLeft(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        cardListService.moveLeft(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveRight/{id}")
    public ResponseEntity<String> moveRight(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        cardListService.moveRight(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCard(@RequestBody CardListPost cardListPost) throws TableNotFoundException {
        cardListService.update(cardListPost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}

