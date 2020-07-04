package com.paw.trello.dao;

import com.paw.trello.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
public interface CardsRepository extends JpaRepository<Cards, Long> {
    Optional<Cards> findById(Long id);
    List<Cards> findAllByLists_Id(Long id);
    List<Cards> findAllByLists_Tables_Id(Long id);
}
