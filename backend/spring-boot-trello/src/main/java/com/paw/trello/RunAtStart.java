package com.paw.trello;

import com.paw.trello.dao.*;
import com.paw.trello.entity.*;
import com.paw.trello.service.ListsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RunAtStart {

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private ListsRepository listsRepository;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private ListsService listsService;

    @PostConstruct
    public void runAtStart() {

        Users usersTemp = new Users();
        usersTemp.setUsername("root");
        usersTemp.setPassword("$2a$10$ckaGf3PJJKBXtAw9/FQgceimEaAwRW9eplk2vovhk11j8bZJGip5q");
        Users users = usersRepository.save(usersTemp);
        Users usersTemp2 = new Users();
        usersTemp2.setUsername("r");
        usersTemp2.setPassword("$2a$10$ckaGf3PJJKBXtAw9/FQgceimEaAwRW9eplk2vovhk11j8bZJGip5q");
        Users users2 = usersRepository.save(usersTemp2);

        Tables table1 = tablesRepository.save(new Tables(1L, "TABLE ONE", users));
        Tables table2 = tablesRepository.save(new Tables(2L, "TABLE TWO", users));
        Tables table3 = tablesRepository.save(new Tables(3L, "TABLE THREE", users2));

        Lists list1 = listsRepository.save(new Lists(1L, "THE LIST 1", table1, 1L));
        Lists list12 = listsRepository.save(new Lists(2L, "THE LIST 2", table1, 2L));
        Lists list13 = listsRepository.save(new Lists(3L, "THE LIST 3", table1, 3L));
        Lists list2 = listsRepository.save(new Lists(4L, "THE LIST 4", table2, 4L));
        Lists list3 = listsRepository.save(new Lists(5L, "THE LIST 5", table3, 5L));

        Cards cards1 = cardsRepository.save(new Cards(1L, "THE CARD 1", "IS WORKING!", list1));
        Cards cards2 = cardsRepository.save(new Cards(2L, "THE CARD 2", "IS WORKING!!", list1));
        Cards cards22 = cardsRepository.save(new Cards(3L, "THE CARD 1", "IS WORKING!", list12));
        Cards car33 = cardsRepository.save(new Cards(4L, "THE CARD 1", "IS WORKING!", list13));
        Cards cards3 = cardsRepository.save(new Cards(5L, "THE CARD 3", "IS WORKING!!!", list2));
        Cards cards4 = cardsRepository.save(new Cards(6L, "THE CARD 4", "IS WORKING!!!!", list2));
        Cards cards5 = cardsRepository.save(new Cards(7L, "THE CARD 5", "IS WORKING!!!!!", list3));
        Cards cards6 = cardsRepository.save(new Cards(8L, "THE CARD 6", "IS WORKING!!!!!!", list3));

        Files file1 = filesRepository.save(new Files(1L, "file1.txt", "text/plain", "testfi 111 letestfi111 letestfile".getBytes(), cards1));
        Files file2 = filesRepository.save(new Files(2L, "file2.txt", "text/plain", "222 testfiletes222 tfiletestfile".getBytes(), cards1));
        Files file3 = filesRepository.save(new Files(3L, "file3.txt", "text/plain", "testfil333 etestfiletestfile333".getBytes(), cards2));
    }
}
