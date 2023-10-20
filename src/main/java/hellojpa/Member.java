package hellojpa;

import javax.persistence.*;
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
public class Member {

    @Id  @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @Column(name="username")        //db 컬럼명
    private String username;

//    @Column(name ="team_id")
//    private Long teamId;

    @ManyToOne //member 입장에서 Team은 하나
    @JoinColumn(name ="team_id")
    private Team team;

    private Integer age;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    @Lob
    private String description;
    
    @Transient  //db랑 관계 없이 사용
    private int temp;
    
    public Member(){

    }
    public Member(Long id, String name){
        this.id = id;
        this.username = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
/*
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
 */

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
