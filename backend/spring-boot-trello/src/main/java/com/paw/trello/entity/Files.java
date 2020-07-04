package com.paw.trello.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mimetype")
    private String mimetype;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(foreignKey = @ForeignKey(name = "cards_id"), name = "cards_id", nullable = false)
    private Cards cards;

    public Files(String name, String mimetype, byte[] pic, Cards cards) {

        this.name = name;
        this.mimetype = mimetype;
        this.pic = pic;
        this.cards = cards;
    }
}
