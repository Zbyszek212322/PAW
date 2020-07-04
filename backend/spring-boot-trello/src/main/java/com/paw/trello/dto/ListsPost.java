package com.paw.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListsPost {
    private String listName;
    private Long table_id;
    private Long cardListId;
}
