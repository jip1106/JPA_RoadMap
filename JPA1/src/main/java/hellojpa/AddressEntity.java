package hellojpa;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="ADDRESS")
public class AddressEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity(String city, String street, String zipcode){
        this.address = new Address(city,street,zipcode);
    }

}
