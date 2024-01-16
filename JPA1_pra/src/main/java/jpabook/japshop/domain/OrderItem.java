package jpabook.japshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="ORDER_ITEM_BEFORE")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name ="ORDER_ITEM_ID")
    private Long id;

    //주문상품 테이블은 주문키를 외래키로 가지고 있음 -> 연관관계 주인 -> 주인이 아닌쪽에 mappedBy 사용
    //주문1 : 주문N
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    //주문1 : 상품N
    //주문상품 테이블은 상품키를 외래키로 가지고 있음 -> 연관관계 주인 -> 주인이 아닌쪽에 mappedBy 사용
    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    private Item item;

    private int orderPrice;
    private int count;


}
