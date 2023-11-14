package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //em.merge(item)와 같음
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity/*, Book bookParam*/){
        Item findItem = itemRepository.findOne(itemId); //실제 db의 영속상태 entity를 조회

        //변경감지가 일어나서 save 호출 필요 없이 업데이트 실행
        // merge()를 사용하지 말고 이런식으로 해야함
        /*
        findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
         */
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);

    }

    @Transactional
    public void updateItemByDTO(UpdateItemDTO dto){
        Item findItem = itemRepository.findOne(dto.getItemId());

        findItem.setName(dto.getName());
        findItem.setPrice(dto.getPrice());
        findItem.setStockQuantity(dto.getStockQuantity());

    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
