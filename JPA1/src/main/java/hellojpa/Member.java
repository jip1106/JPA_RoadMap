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


 ** 기본키 매핑
 • 자동 생성(@GeneratedValue)
    • IDENTITY: 데이터베이스에 위임, MYSQL
    • SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE
    • @SequenceGenerator 필요
    • TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용
    • @TableGenerator 필요
    • AUTO: 방언에 따라 자동 지정, 기본값


  연관관계를 지정할때 익숙한 테이블 설계를 먼저 하고,
  외래키가 있는곳을 주인으로 지정하자
  외래키가 없는곳은 @OneToMany(mappedBy="") 속성을 사용하고
  외래키가 있는 주인은 @ManyToOne(fetch = FetchType.LAZY) 를 사용하자 ( 외래키를 키로 관리하지 말고 객체로 관리)
*/

@Entity //@Entity가 붙은 클래스는 JPA가 관리
@Table(name="MEMBER")   //@Table(name="CO_MEMBER") 테이블에 맵핑
@Getter
@Setter
public class Member extends BaseEntity{

    // 기본 생성자는 필수
    public Member(){

    }
    public Member(Long id, String name){
        this.id = id;
        this.username = name;
    }

    //@Column(name = "member_id") db에는 member_id로 사용, java에선 id 변수명으로 사용
    @Id
    @GeneratedValue
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)   //주로 ORACLE에서 사용 -> 키에 맞는 sequence를 만들어 냄
    //@GeneratedValue(strategy = GenerationType.IDENTITY)   //DB방언에 맞춰서 생성
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
    
    @Transient  //db랑 관계 없이 메모리 에서 사용
    private int temp;
    /*
    private String createdBy;
    private String modifiedBy;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

     */



    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

}
