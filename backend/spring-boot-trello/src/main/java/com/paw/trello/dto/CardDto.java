package com.paw.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto implements Comparable<CardDto> {
    private Long id;
    private String title;
    private String description;
    private CardListDto list;

    @Override
    public int compareTo(CardDto o) {
        return this.getId().compareTo(o.getId());
    }
}
