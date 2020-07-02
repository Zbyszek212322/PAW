package com.paw.trello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="table_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ttable")
    private Set<CardList> ttables;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "mimetype")
    private String mimetype;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    public TableList(Long id, String tableName, User user) {

        this.id = id;
        this.tableName = tableName;
        this.user = user;
    }
}
