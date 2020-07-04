package com.paw.trello.service;

import com.paw.trello.dao.CardsRepository;
import com.paw.trello.dao.ListsRepository;
import com.paw.trello.dto.CardAddPost;
import com.paw.trello.dto.CardDto;
import com.paw.trello.dto.CardUpdatePost;
import com.paw.trello.entity.Cards;
import com.paw.trello.entity.Lists;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CardsService {

    private final CardsRepository cardsRepository;
    private final ListsRepository listsRepository;

    @Autowired
    public CardsService(CardsRepository cardsRepository, ListsRepository listsRepository) {
        super();
        this.cardsRepository = cardsRepository;
        this.listsRepository = listsRepository;
    }

    public CardDto findById(Long id) throws TableNotFoundException {
        Cards cards = cardsRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromTableListToDto(cards);
    }

    public Iterable<CardDto> findAll() {
        List<Cards> cardsList = cardsRepository.findAll();
        return cardsList.stream().map(CardsService::mapFromTableListToDto).collect(toList());
    }

    public List<CardDto> findAllCardsFromList(Long id) {
        List<Cards> cardsList = cardsRepository.findAllByLists_Id(id);
        return cardsList.stream().map(CardsService::mapFromTableListToDto).sorted().collect(toList());
    }

    public List<CardDto> findAllCardsByTable(Long id) {
        List<Cards> cardsList = cardsRepository.findAllByLists_Tables_Id(id);
        return cardsList.stream().map(CardsService::mapFromTableListToDto).sorted().collect(toList());
    }

    public Cards save(Cards cards) {
        return cardsRepository.save(cards);
    }

    public void deleteById(Long id) {
        cardsRepository.deleteById(id);
    }

    public Cards add(CardAddPost cardPost) throws TableNotFoundException {
        Cards cards = new Cards();
        cards.setTitle(cardPost.getCardName());
        cards.setDescription(cardPost.getDescription());
        cards.setLists(listsRepository.findById(cardPost.getCardListId()).orElseThrow(() -> new TableNotFoundException("Brak listy")));
        return cardsRepository.save(cards);
    }

    public void update(CardUpdatePost cardUpdatePost) throws TableNotFoundException {
        Cards cards = cardsRepository.findById(cardUpdatePost.getCardId()).orElseThrow(() -> new TableNotFoundException("Brak karty "));
        cards.setTitle(cardUpdatePost.getCardName());
        cards.setDescription(cardUpdatePost.getDescription());
        cardsRepository.save(cards);
    }



    public void moveRight (Long id) {
        Optional<Cards> cardOptional = cardsRepository.findById(id);
        Cards cards = cardOptional.get();
        Long tableId = cards.getLists().getTables().getId();
        List<Lists> lists = listsRepository.findAllByTables_IdOrderByOrderNo(tableId);

        for(int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getId().equals(cards.getLists().getId()) && i < lists.size() - 1) {
                cards.setLists(lists.get(i + 1));
                cardsRepository.save(cards);
                break;
            }
        }
    }

    public void moveLeft (Long id) {
        Optional<Cards> cardOptional = cardsRepository.findById(id);
        Cards cards = cardOptional.get();
        Long tableId = cards.getLists().getTables().getId();
        List<Lists> lists = listsRepository.findAllByTables_IdOrderByOrderNo(tableId);

        for(int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getId().equals(cards.getLists().getId()) && i > 0) {
                cards.setLists(lists.get(i - 1));
                cardsRepository.save(cards);
                break;
            }
        }
    }

    public static CardDto mapFromTableListToDto(Cards cards) {
        CardDto cardDto = new CardDto();
        cardDto.setId(cards.getId());
        cardDto.setTitle(cards.getTitle());
        cardDto.setDescription(cards.getDescription());
        cardDto.setList(ListsService.mapFromListsToDto(cards.getLists()));
        return cardDto;
    }
}
