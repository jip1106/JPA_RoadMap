package jpabook.japshop.domain2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Item")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 싱글테이블 전략일땐 ITEM 테이블만 만들어지는게 정상 상속받은 BOOK, Album 테이블 등은 안만들어짐
//@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Item extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
