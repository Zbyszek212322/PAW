package com.paw.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardListDto implements Comparable<CardListDto> {
    private Long id;
    private String listName;
    private boolean archive;
    private Long orderNo;

    @Override
    public int compareTo(CardListDto o) {
        return this.getOrderNo().compareTo(o.getOrderNo());
    }
}
