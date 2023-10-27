package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*
 JOINED: 조인 전략
• SINGLE_TABLE: 단일 테이블 전략
• TABLE_PER_CLASS: 구현 클래스마다 테이블 전략
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED) //조인 전략
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 단일 테이블 전략
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 구현 클래스마다 테이블 전략 => 조회시 문제(union 으로 조회) 쓰면안됌
@DiscriminatorColumn(name = "DIS_TYPE")
@Getter
@Setter
public class Item {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
