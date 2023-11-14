package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //@JsonIgnore //엔티티에 화면을 뿌리기 위한 로직이 추가되어버림... 권장 X
    @OneToMany(mappedBy = "member") //읽기전용 Order 테이블의 member 변수
    private List<Order> orders = new ArrayList<>();
}
