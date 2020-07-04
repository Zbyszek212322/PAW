package com.paw.trello.dao;


import com.paw.trello.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface FilesRepository extends JpaRepository<Files, Long> {
    List<Files> findAllByCards_Id(Long id);
}
