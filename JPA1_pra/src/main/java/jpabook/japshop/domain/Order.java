package jpabook.japshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="ORDER_BEFORE")
@Table(name = "ORDERS")
@Getter
@Setter
public class Order {
    @Id    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    //주문테이블에 memberId 를 외래키로 가지고 있음
    // -> 외래키를 가지고 있는 테이블이 연관관계 주인
    // -> Member에 @OneToMany mappedBy를 걸자!
    //private Long memberId;

    @ManyToOne
    @JoinColumn(name ="MEMBER_ID")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //주문과 상품의 관계 1:N
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }


    //lombok 변경
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Member getMember() {
//        return member;
//    }
//
//    public void setMember(Member member) {
//        this.member = member;
//    }
//
//    public LocalDateTime getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(LocalDateTime orderDate) {
//        this.orderDate = orderDate;
//    }
//
//    public OrderStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(OrderStatus status) {
//        this.status = status;
//    }
}
