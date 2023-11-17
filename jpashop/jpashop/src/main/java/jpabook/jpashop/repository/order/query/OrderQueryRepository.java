package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();  //주문을 찾는 쿼리 1번 -> 주문 N개

        //컬렉션 부분은 따로 직접 채움
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());    //주문당 엮여있는 상품을 찾는 쿼리 N번 N+1문제 발생
            o.setOrderItems(orderItems);
        });

        return result;
    }


    // orderItem 을 가져올때 in 쿼리로 가져오도록 만듦
    public List<OrderQueryDto> findAllByDto_optimization() {
        //주문 조회쿼리
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = toOrderIds(result);

        //주문에 엮인 주문상품 쿼리를 한번만 보내서 가져온 후
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("Select "
                        + " new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
                        + " from OrderItem oi "
                        + " join oi.item i"
                        + " Where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //메모리상에서 처리함
        //orderItems 를 map 으로 바꿔줌 (성능 최적화 하기 위해)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream().map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }


    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("Select "
        + " new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
        + " from OrderItem oi "
        + " join oi.item i"
        + " Where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery("Select "
        + " new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) "
        + " from Order o"
        + " join o.member m"
        + " join o.delivery d", OrderQueryDto.class).getResultList();

    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery("select new " +
                " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " From Order o" +
                " join o.member m " +
                " join o.delivery d" +
                " join o.orderItems oi " +
                " join oi.item i" , OrderFlatDto.class)
                .getResultList();
    }
}
