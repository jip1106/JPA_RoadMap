package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;


@Embeddable
@Getter
public class Address {

    private String city;
    private String stree;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String stree, String zipcode) {
        this.city = city;
        this.stree = stree;
        this.zipcode = zipcode;
    }
}
