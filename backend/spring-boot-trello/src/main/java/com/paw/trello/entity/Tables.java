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
public class Tables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tables")
    private Set<Lists> tables;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @Column(name = "mimetype")
    private String mimetype;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    public Tables(Long id, String tableName, Users users) {

        this.id = id;
        this.tableName = tableName;
        this.users = users;
    }
}
