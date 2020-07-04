package com.paw.trello.dao;

import com.paw.trello.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Set;

@CrossOrigin("http://localhost:4200")
public interface TablesRepository extends JpaRepository<Tables, Long> {
    Set<Tables> getAllByUsersId(Long id);
}
