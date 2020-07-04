package com.paw.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListsDto implements Comparable<ListsDto> {
    private Long id;
    private String listName;
    private boolean archive;
    private Long orderNo;

    @Override
    public int compareTo(ListsDto o) {
        return this.getOrderNo().compareTo(o.getOrderNo());
    }
}
