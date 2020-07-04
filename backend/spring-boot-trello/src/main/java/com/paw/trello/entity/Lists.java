package com.paw.trello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lists implements Comparable<Lists> {

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
    @JoinColumn(foreignKey = @ForeignKey(name = "tables_id"), name = "tables_id", nullable = false)
    private Tables tables;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lists")
    private Set<Cards> cards;

    public Lists(Long id, String listName, Tables ttable, Long orderNo) {
        this.id = id;
        this.listName = listName;
        this.tables = ttable;
        this.orderNo = orderNo;
    }

    @Override
    public int compareTo(Lists o) {
        return this.getOrderNo().compareTo(o.getOrderNo());
    }
}
