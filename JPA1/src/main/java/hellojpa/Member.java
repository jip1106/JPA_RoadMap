package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/*
  @Entity가 붙은 클래스는 JPA가 관리, 엔티티라 한다.
  JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수

  - 기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)
  - final 클래스, enum, interface, inner 클래스 사용 X
  - 저장할 필드에 final 사용 X
*/

@Entity //@Entity가 붙은 클래스는 JPA가 관리
@Table(name="MEMBER")   //name="CO_MEMBER" 테이블에 맵핑
@Getter
@Setter
public class Member extends BaseEntity{

    @Id  @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Column(name="username")        //db 컬럼명
    private String username;

//    @Column(name ="team_id")
//    private Long teamId;

    //외래키가 있는 곳을 주인으로 지정, 주인은 mappedBy 속성 사용X , 주인이 아니면 mappedBy 속성으로 주인 지정
    //팀1 -> 회원이 N명
    //FetchType.LAZY 옵션을 주면, Member를 조회할때 Team을 같이 조회 안하고 Member만 조회
    //@ManyToOne(fetch = FetchType.LAZY) //member 입장에서 Team은 하나 (지연로딩)
    @ManyToOne(fetch = FetchType.EAGER)//member 입장에서 Team은 하나 (즉시로딩)
    //@JoinColumn(name ="team_id", insertable = false, updatable = false)
    @JoinColumn(name ="team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Lob
    private String description;
    
    @Transient  //db랑 관계 없이 사용
    private int temp;
    /*
    private String createdBy;
    private String modifiedBy;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

     */

    
    public Member(){

    }
    public Member(Long id, String name){
        this.id = id;
        this.username = name;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

}
