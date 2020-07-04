package com.paw.trello.controller;

import com.paw.trello.dto.CardAddPost;
import com.paw.trello.dto.CardDto;
import com.paw.trello.dto.CardUpdatePost;
import com.paw.trello.entity.Cards;
import com.paw.trello.exceptions.TableNotFoundException;
import com.paw.trello.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private CardsService cardsService;

    @Autowired
    public CardController(CardsService cardsService) {
        this.cardsService = cardsService;
    }

    @GetMapping("/all")
    public Iterable<CardDto> getAll() {
        return cardsService.findAll();
    }

    @GetMapping("/get/{id}")
    public CardDto getById(@PathVariable @RequestBody Long id) throws TableNotFoundException {
        return cardsService.findById(id);
    }

    @GetMapping("/get/cardList/{id}")
    public List<CardDto> getByIdd(@PathVariable @RequestBody Long id) {
        return cardsService.findAllCardsFromList(id);
    }

    @GetMapping("/get/table/{id}")
    public List<CardDto> getByTable(@PathVariable @RequestBody Long id) {
        return cardsService.findAllCardsByTable(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCard(@RequestBody CardAddPost cardPost) throws TableNotFoundException {
        cardsService.add(cardPost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCard(@RequestBody CardUpdatePost cardUpdatePost) throws TableNotFoundException {
        cardsService.update(cardUpdatePost);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCard(@RequestParam(name = "cardId") Long cardId) {
        cardsService.deleteById(cardId);
        return new ResponseEntity<>("Card with ID:" + cardId + " deleted successfully", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Cards> updateCard(@RequestBody Cards cards) {
        Cards cardd = cardsService.save(cards);
        return  new ResponseEntity<>(cardd, HttpStatus.OK);
    }

    @PutMapping("/moveRight/{id}")
    public ResponseEntity<String> moveRight(@PathVariable @RequestBody Long id) {
        cardsService.moveRight(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/moveLeft/{id}")
    public ResponseEntity<String> moveLeft(@PathVariable @RequestBody Long id) {
        cardsService.moveLeft(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
