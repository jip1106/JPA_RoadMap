package jpabook.jpashop.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateItemDTO {

    private Long itemId;
    private String name;
    private int price;
    private int stockQuantity;
}
