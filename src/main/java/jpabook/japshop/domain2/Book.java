package jpabook.japshop.domain2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table
public class Book extends Item{
    private String author;
    private String isbn;
}
