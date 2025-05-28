package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

// Mybatis 의 경우 @Mapper 어노테이션을 붙여줘야 인식
@Mapper
public interface ItemMapper {

    void save(Item item);

    // 파라미터가 2개 이상 시 @Param 을 넣어줘야 함
    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);
    
    // 반환 객체가 하나 인 경우 Item 혹은 Optional<Item> 사용
    Optional<Item> findById(Long id); 

    // 반환 객체가 여러 개 일 경우 보통 List 사용
    List<Item> findAll(ItemSearchCond itemSearch);
}
