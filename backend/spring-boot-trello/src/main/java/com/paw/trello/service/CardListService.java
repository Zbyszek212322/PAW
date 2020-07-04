package com.paw.trello.service;

import com.paw.trello.dao.CardListRepository;
import com.paw.trello.dao.TableListRepository;
import com.paw.trello.dto.CardListDto;
import com.paw.trello.dto.CardListPost;
import com.paw.trello.entity.Card;
import com.paw.trello.entity.CardList;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardListService {


    private static Long orderNo = 6L;

    private final CardListRepository cardListRepository;
    private final TableListRepository tableListRepository;

    @Autowired
    public CardListService(CardListRepository cardListRepository, TableListRepository tableListRepository) {
        super();
        this.cardListRepository = cardListRepository;
        this.tableListRepository = tableListRepository;
    }

    public CardListDto findById(Long id) throws TableNotFoundException {
        CardList cardList = cardListRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromCardListToDto(cardList);
    }

    public List<CardListDto> findAll() {
        List<CardList> cardLists = cardListRepository.findAll();
        return cardLists.stream().map(CardListService::mapFromCardListToDto).collect(toList());
    }

    public List<CardListDto> findAllCardListsFromTable(Long id) {
        List<CardList> cardLists = cardListRepository.findAllByTtable_Id(id);

        return cardLists.stream().map(CardListService::mapFromCardListToDto).sorted().collect(toList());
    }

    public void moveLeft (Long id) {
        CardList cardList = cardListRepository.findAllById(id);
        Long tableId = cardList.getTtable().getId();
        List<CardList> cardLists = cardListRepository.findAllByTtable_IdOrderByOrderNo(tableId);

        for(int i = 0; i < cardLists.size(); i++) {
            if(cardLists.get(i).getId().equals(cardList.getId()) && i > 0) {
                CardList cardList2 = cardLists.get(i - 1);
                Long orderNo = cardList.getOrderNo();
                cardList.setOrderNo(cardList2.getOrderNo());
                cardListRepository.save(cardList);
                cardList2.setOrderNo(orderNo);
                cardListRepository.save(cardList2);
                break;
            }
        }
    }

    public void moveRight (Long id) {
        CardList cardList = cardListRepository.findAllById(id);
        Long tableId = cardList.getTtable().getId();
        List<CardList> cardLists = cardListRepository.findAllByTtable_IdOrderByOrderNo(tableId);

        for(int i = 0; i < cardLists.size(); i++) {
            if(cardLists.get(i).getId().equals(cardList.getId()) && i < cardLists.size() - 1) {
                CardList cardList2 = cardLists.get(i + 1);
                Long orderNo = cardList.getOrderNo();
                cardList.setOrderNo(cardList2.getOrderNo());
                cardListRepository.save(cardList);
                cardList2.setOrderNo(orderNo);
                cardListRepository.save(cardList2);
                break;
            }
        }
    }

    public CardList add(CardListPost cardListPost) throws TableNotFoundException {
        CardList cardList = new CardList();
        cardList.setListName(cardListPost.getListName());
        cardList.setOrderNo(orderNo);
        orderNo++;
        cardList.setTtable(tableListRepository.findById(cardListPost.getTable_id()).orElseThrow(() -> new TableNotFoundException("Brak tabeli")));
        return cardListRepository.save(cardList);
    }

    public static CardListDto mapFromCardListToDto(CardList cardList) {
        CardListDto cardListDto = new CardListDto();
        cardListDto.setId(cardList.getId());
        cardListDto.setListName(cardList.getListName());
        cardListDto.setArchive(cardList.isArchive());
        cardListDto.setOrderNo(cardList.getOrderNo());
        return cardListDto;
    }

    public CardList archive(Long id) throws TableNotFoundException {
        CardList cardList = cardListRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak listy"));
        cardList.setArchive(true);
        return cardListRepository.save(cardList);
    }

    public void update(CardListPost cardListPost) throws TableNotFoundException {
        CardList cardList = cardListRepository.findById(cardListPost.getCardListId()).orElseThrow(() -> new TableNotFoundException("Brak listy "));
        cardList.setListName(cardListPost.getListName());
        cardList.setTtable(tableListRepository.findById(cardListPost.getTable_id()).orElseThrow(() -> new TableNotFoundException("Brak TABELI ")));
        cardListRepository.save(cardList);
    }
}
