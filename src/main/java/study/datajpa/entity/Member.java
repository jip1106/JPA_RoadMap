package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
//엔티티에 NamedQuery를 적어서 호출
@NamedQuery(
        name="Member.findByUsername",
        query="select m From Member m Where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity/*JpaBaseEntity*/{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }

    }

    public void changeUserName(String username){
        this.username = username;
    }

    //연관관계 메서드
    //회원이 팀 변경
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
