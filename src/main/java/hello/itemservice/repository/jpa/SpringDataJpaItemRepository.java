package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    // 쿼리 메서드 (아래 메서드와 같은 기능을 수행)
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    // 쿼리 직접 실행 (@Param 을 넣어서 바인딩 해줘야 제대로 적용)
    @Query("SELECT i FROM Item i WHERE i.itemName LIKE  :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String ItemName, @Param("price") Integer price);
}
