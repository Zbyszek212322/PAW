package com.paw.trello.dao;

import com.paw.trello.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface ListsRepository extends JpaRepository<Lists, Long> {
    List<Lists> findAllByTables_Id(Long id);
    List<Lists> findAllByTables_IdOrderByOrderNo(Long id);
    Lists findAllById(Long id);
}
