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
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(foreignKey = @ForeignKey(name = "lists_id"), name = "lists_id", nullable = false)
    private Lists lists;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cards")
    private Set<Files> files;

    public Cards(Long id, String title, String description, Lists list) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.lists = list;
    }
}