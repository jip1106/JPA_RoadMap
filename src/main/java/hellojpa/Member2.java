package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="MEMBER2")
@Getter
@Setter
public class Member2{

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name="USERNAME")
    private String username;

    //기간 Period
//    private LocalDateTime startDate;
//    private LocalDateTime endDate;
    @Embedded
    private Period workPeriod;

    //주소
//    private String city;
//    private String stree;
//    private String zipcode;
    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name="street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name="zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;


}
