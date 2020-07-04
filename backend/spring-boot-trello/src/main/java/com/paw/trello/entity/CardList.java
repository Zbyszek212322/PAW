package com.paw.trello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="card_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardList implements Comparable<CardList> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "list_name")
    private String listName;

    @Column(name = "is_archive")
    private boolean isArchive = false;

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "order_number")
    private Long orderNo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(foreignKey = @ForeignKey(name = "ttable_id"), name = "ttable_id", nullable = false)
    private TableList ttable;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "list")
    private Set<Card> cards;

    public CardList(Long id, String listName, TableList ttable, Long orderNo) {
        this.id = id;
        this.listName = listName;
        this.ttable = ttable;
        this.orderNo = orderNo;
    }

    @Override
    public int compareTo(CardList o) {
        return this.getOrderNo().compareTo(o.getOrderNo());
    }
}
