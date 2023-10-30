package jpabook.japshop.domain2;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseEntity{

    @Id @GeneratedValue
    @JoinColumn(name="MEMBER_ID")
    private Long id;
    @OneToOne
    @JoinColumn(name="DELIVERY_ID")
    private Delivery delivery;
    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);

    }
}
