package com.paw.trello.service;

import com.paw.trello.dao.CardListRepository;
import com.paw.trello.dao.CardRepository;
import com.paw.trello.dto.CardDto;
import com.paw.trello.dto.CardPost;
import com.paw.trello.entity.Card;
import com.paw.trello.entity.CardList;
import com.paw.trello.entity.TableList;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Set<CardDto> findAllCardsFromList(Long id) {
        Set<Card> cardList = cardRepository.findAllByList_Id(id);
        return cardList.stream().map(CardService::mapFromTableListToDto).collect(toSet());
    }

    public Set<CardDto> findAllCardsByTable(Long id) {
        Set<Card> cardList = cardRepository.findAllByList_Ttable_Id(id);
        return cardList.stream().map(CardService::mapFromTableListToDto).collect(toSet());
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    public Card add(CardPost cardPost) throws TableNotFoundException {
        Card card = new Card();
        card.setTitle(cardPost.getCardName());
        card.setDescription(cardPost.getDescription());
        card.setList(cardListRepository.findById(cardPost.getCardListId()).orElseThrow(() -> new TableNotFoundException("Brak listy")));
        return cardRepository.save(card);
    }

    public void update(CardPost cardPost, Long id) throws TableNotFoundException {
        Card card = cardRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak karty "));
        card.setTitle(cardPost.getCardName());
        card.setDescription(cardPost.getDescription());
        cardRepository.save(card);
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
