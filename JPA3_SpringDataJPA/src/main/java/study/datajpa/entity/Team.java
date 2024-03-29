package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Team extends JpaBaseEntity{

    @Id @GeneratedValue
    @Column(name="team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")   //외래키가 없는쪽에 mappedBy를 건다
    private List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }
}
