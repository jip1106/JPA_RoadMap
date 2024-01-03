package jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable //값 타입
@Getter
@Setter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
