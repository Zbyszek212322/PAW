package com.paw.trello.service;

import com.paw.trello.dao.ListsRepository;
import com.paw.trello.dao.TablesRepository;
import com.paw.trello.dto.ListsDto;
import com.paw.trello.dto.ListsPost;
import com.paw.trello.entity.Lists;
import com.paw.trello.exceptions.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ListsService {


    private static Long orderNo = 6L;

    private final ListsRepository listsRepository;
    private final TablesRepository tablesRepository;

    @Autowired
    public ListsService(ListsRepository listsRepository, TablesRepository tablesRepository) {
        super();
        this.listsRepository = listsRepository;
        this.tablesRepository = tablesRepository;
    }

    public ListsDto findById(Long id) throws TableNotFoundException {
        Lists lists = listsRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak tabeli " + id));
        return mapFromListsToDto(lists);
    }

    public List<ListsDto> findAll() {
        List<Lists> lists = listsRepository.findAll();
        return lists.stream().map(ListsService::mapFromListsToDto).collect(toList());
    }

    public List<ListsDto> findAllCardListsFromTable(Long id) {
        List<Lists> lists = listsRepository.findAllByTables_Id(id);

        return lists.stream().map(ListsService::mapFromListsToDto).sorted().collect(toList());
    }

    public void moveLeft (Long id) {
        Lists list = listsRepository.findAllById(id);
        Long tableId = list.getTables().getId();
        List<Lists> lists = listsRepository.findAllByTables_IdOrderByOrderNo(tableId);

        for(int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getId().equals(list.getId()) && i > 0) {
                Lists lists2 = lists.get(i - 1);
                Long orderNo = list.getOrderNo();
                list.setOrderNo(lists2.getOrderNo());
                listsRepository.save(list);
                lists2.setOrderNo(orderNo);
                listsRepository.save(lists2);
                break;
            }
        }
    }

    public void moveRight (Long id) {
        Lists list = listsRepository.findAllById(id);
        Long tableId = list.getTables().getId();
        List<Lists> lists = listsRepository.findAllByTables_IdOrderByOrderNo(tableId);

        for(int i = 0; i < lists.size(); i++) {
            if(lists.get(i).getId().equals(list.getId()) && i < lists.size() - 1) {
                Lists lists2 = lists.get(i + 1);
                Long orderNo = list.getOrderNo();
                list.setOrderNo(lists2.getOrderNo());
                listsRepository.save(list);
                lists2.setOrderNo(orderNo);
                listsRepository.save(lists2);
                break;
            }
        }
    }

    public Lists add(ListsPost cardListPost) throws TableNotFoundException {
        Lists lists = new Lists();
        lists.setListName(cardListPost.getListName());
        lists.setOrderNo(orderNo);
        orderNo++;
        lists.setTables(tablesRepository.findById(cardListPost.getTable_id()).orElseThrow(() -> new TableNotFoundException("Brak tabeli")));
        return listsRepository.save(lists);
    }

    public static ListsDto mapFromListsToDto(Lists lists) {
        ListsDto listsDto = new ListsDto();
        listsDto.setId(lists.getId());
        listsDto.setListName(lists.getListName());
        listsDto.setArchive(lists.isArchive());
        listsDto.setOrderNo(lists.getOrderNo());
        return listsDto;
    }

    public Lists archive(Long id) throws TableNotFoundException {
        Lists lists = listsRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Brak listy"));
        lists.setArchive(true);
        return listsRepository.save(lists);
    }

    public void update(ListsPost cardListPost) throws TableNotFoundException {
        Lists lists = listsRepository.findById(cardListPost.getCardListId()).orElseThrow(() -> new TableNotFoundException("Brak listy "));
        lists.setListName(cardListPost.getListName());
        lists.setTables(tablesRepository.findById(cardListPost.getTable_id()).orElseThrow(() -> new TableNotFoundException("Brak TABELI ")));
        listsRepository.save(lists);
    }
}
