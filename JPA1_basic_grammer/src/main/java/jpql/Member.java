package jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m Where m.username = :username"
)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }


    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
