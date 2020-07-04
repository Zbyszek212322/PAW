package com.paw.trello;

import com.paw.trello.dao.*;
import com.paw.trello.entity.*;
import com.paw.trello.service.CardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RunAtStart {

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private CardListRepository cardListRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CardListService cardListService;

    @PostConstruct
    public void runAtStart() {

        User userTemp = new User();
        userTemp.setUsername("root");
        userTemp.setPassword("$2a$10$ckaGf3PJJKBXtAw9/FQgceimEaAwRW9eplk2vovhk11j8bZJGip5q");
        User user = userRepository.save(userTemp);
        User userTemp2 = new User();
        userTemp2.setUsername("r");
        userTemp2.setPassword("$2a$10$ckaGf3PJJKBXtAw9/FQgceimEaAwRW9eplk2vovhk11j8bZJGip5q");
        User user2 = userRepository.save(userTemp2);

        TableList table1 = tableListRepository.save(new TableList(1L, "TABLE ONE", user));
        TableList table2 = tableListRepository.save(new TableList(2L, "TABLE TWO", user));
        TableList table3 = tableListRepository.save(new TableList(3L, "TABLE THREE", user2));

        CardList list1 = cardListRepository.save(new CardList(1L, "THE LIST 1", table1, 1L));
        CardList list12 = cardListRepository.save(new CardList(2L, "THE LIST 2", table1, 2L));
        CardList list13 = cardListRepository.save(new CardList(3L, "THE LIST 3", table1, 3L));
        CardList list2 = cardListRepository.save(new CardList(4L, "THE LIST 4", table2, 4L));
        CardList list3 = cardListRepository.save(new CardList(5L, "THE LIST 5", table3, 5L));

        Card card1 = cardRepository.save(new Card(1L, "THE CARD 1", "IS WORKING!", list1));
        Card card2 = cardRepository.save(new Card(2L, "THE CARD 2", "IS WORKING!!", list1));
        Card card22 = cardRepository.save(new Card(3L, "THE CARD 1", "IS WORKING!", list12));
        Card car33 = cardRepository.save(new Card(4L, "THE CARD 1", "IS WORKING!", list13));
        Card card3 = cardRepository.save(new Card(5L, "THE CARD 3", "IS WORKING!!!", list2));
        Card card4 = cardRepository.save(new Card(6L, "THE CARD 4", "IS WORKING!!!!", list2));
        Card card5 = cardRepository.save(new Card(7L, "THE CARD 5", "IS WORKING!!!!!", list3));
        Card card6 = cardRepository.save(new Card(8L, "THE CARD 6", "IS WORKING!!!!!!", list3));

        FileModel file1 = fileRepository.save(new FileModel(1L, "file1.txt", "text/plain", "testfi 111 letestfi111 letestfile".getBytes(), card1));
        FileModel file2 = fileRepository.save(new FileModel(2L, "file2.txt", "text/plain", "222 testfiletes222 tfiletestfile".getBytes(), card1));
        FileModel file3 = fileRepository.save(new FileModel(3L, "file3.txt", "text/plain", "testfil333 etestfiletestfile333".getBytes(), card2));
    }
}
