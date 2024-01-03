package jpabook.japshop.domain2;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseEntity{

    @Id @GeneratedValue
    @JoinColumn(name="MEMBER_ID")
    private Long id;

    //order를 저장 할때  delivery를 같이 저장한다-> cascade = CascadeType.ALL
    @OneToOne(fetch=LAZY, cascade =  ALL)
    @JoinColumn(name="DELIVERY_ID")
    private Delivery delivery;
    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    //order를 저장 할때  OrderItem 를 같이 저장한다-> cascade = CascadeType.ALL
    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);

    }
}
