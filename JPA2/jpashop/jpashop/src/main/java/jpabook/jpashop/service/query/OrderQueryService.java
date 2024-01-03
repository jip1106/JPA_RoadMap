package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


//open-in-view: false 설정을 하면 쿼리를 아예 분리하는게 좋다
//읽기전용 트랜잭션 사용
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        //OrderDto 안에서 items를 가져옴
        List<OrderDto> orderItems = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return orderItems;
    }



}
