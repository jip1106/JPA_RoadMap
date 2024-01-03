package hellojpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")  //팀 1 : 멤버 N    mappedBy => Member의 Team team 변수
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member){
        member.setTeam(this);
        members.add(member);
    }


    /*
        연관 관계의 주인과 mappedBy
        객체와 테이블의 연관 관계를 맺는 차이를 이해 해야함

        - 객체 연관 관계 (2개)
            회원 -> 팀 연관 관계 1개
            팀 -> 회원 연관 관계 1개

        - 테이블 연관 관계 (1개)
            회원 <-> 팀의 연관 관계 1개(양방향)

            객체의 양방향 관계는 서로 다른 단방향 관계 2개

        연관 관계의 주인(Owner)
    */


}
