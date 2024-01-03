package jpabook.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    //DTO로 바로 조회하는 기능
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select " +
                        " new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"+
                        " o From Order o"+
                        " join o.member m "+
                        " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
