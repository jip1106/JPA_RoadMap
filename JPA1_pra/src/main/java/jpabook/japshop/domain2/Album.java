package jpabook.japshop.domain2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table
public class Album extends Item{

    private String artist;
    private String etc;
}
