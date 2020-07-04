package com.paw.trello.service;

import com.paw.trello.dao.CardListRepository;
import com.paw.trello.dao.CardRepository;
import com.paw.trello.dto.CardAddPost;
import com.paw.trello.dto.CardDto;
import com.paw.trello.dto.CardUpdatePost;
import com.paw.trello.entity.Card;
import com.paw.trello.entity.CardList;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    @Autowired
    public CardService(CardRepository cardRepository, CardListRepository cardListRepository) {
        super();
        this.cardRepository = cardRepository;
        this.cardListRepository = cardListRepository;
    }

    public CardDto findById(Long id) throws TableNotFoundException {
        Card card = cardRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromTableListToDto(card);
    }

    public Iterable<CardDto> findAll() {
        List<Card> cardList = cardRepository.findAll();
        return cardList.stream().map(CardService::mapFromTableListToDto).collect(toList());
    }

    public List<CardDto> findAllCardsFromList(Long id) {
        List<Card> cardList = cardRepository.findAllByList_Id(id);
        return cardList.stream().map(CardService::mapFromTableListToDto).sorted().collect(toList());
    }

    public List<CardDto> findAllCardsByTable(Long id) {
        List<Card> cardList = cardRepository.findAllByList_Ttable_Id(id);
        return cardList.stream().map(CardService::mapFromTableListToDto).sorted().collect(toList());
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    public Card add(CardAddPost cardPost) throws TableNotFoundException {
        Card card = new Card();
        card.setTitle(cardPost.getCardName());
        card.setDescription(cardPost.getDescription());
        card.setList(cardListRepository.findById(cardPost.getCardListId()).orElseThrow(() -> new TableNotFoundException("Brak listy")));
        return cardRepository.save(card);
    }

    public void update(CardUpdatePost cardUpdatePost) throws TableNotFoundException {
        Card card = cardRepository.findById(cardUpdatePost.getCardId()).orElseThrow(() -> new TableNotFoundException("Brak karty "));
        card.setTitle(cardUpdatePost.getCardName());
        card.setDescription(cardUpdatePost.getDescription());
        cardRepository.save(card);
    }



    public void moveRight (Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        Card card = cardOptional.get();
        Long tableId = card.getList().getTtable().getId();
        List<CardList> cardLists = cardListRepository.findAllByTtable_IdOrderByOrderNo(tableId);

        for(int i = 0; i < cardLists.size(); i++) {
            if(cardLists.get(i).getId().equals(card.getList().getId()) && i < cardLists.size() - 1) {
                card.setList(cardLists.get(i + 1));
                cardRepository.save(card);
                break;
            }
        }
    }

    public void moveLeft (Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        Card card = cardOptional.get();
        Long tableId = card.getList().getTtable().getId();
        List<CardList> cardLists = cardListRepository.findAllByTtable_IdOrderByOrderNo(tableId);

        for(int i = 0; i < cardLists.size(); i++) {
            if(cardLists.get(i).getId().equals(card.getList().getId()) && i > 0) {
                card.setList(cardLists.get(i - 1));
                cardRepository.save(card);
                break;
            }
        }
    }

    public static CardDto mapFromTableListToDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setTitle(card.getTitle());
        cardDto.setDescription(card.getDescription());
        cardDto.setList(CardListService.mapFromCardListToDto(card.getList()));
        return cardDto;
    }
}
