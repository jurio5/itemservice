package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.item;

@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String likeName = "%" + itemName + "%";

//        QItem item = new QItem("i"); // "i" 는 알리아스, 이렇게 사용을 해도 괜찮고
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like(likeName));
        }

        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice)); // loe -> <= , goe -> >=
        }

        return query.select(item) // QItem 객체를 만들 때 내부에 알리아스를 하나 만들어두기에 이걸 사용해도 괜찮음
                .from(item) // 알리아스는 정적으로 선언되어 있어서 static Import 를 등록하여 사용할 수 있음
                .where(builder)
                .fetch();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) { // 리팩토링 버전
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return query.select(item)
                .from(item)
                // , 로 분리 시 AND 조건으로 처리 / null 을 입력 시 조건 무시
                // 가장 큰 장점은 쿼리 조건을 부분적으로 모듈화 할 수 있기에 재사용이 가능
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
//        String likeName = "%" + itemName + "%";
//
//        if (StringUtils.hasText(itemName)) {
//            return item.itemName.like(likeName);
//        }
//        return null;
//        return StringUtils.hasText(itemName) ? item.itemName.like(likeName) : null;
        // 최종적으로 contains 를 사용하는게 더욱 안전
        // contains 의 경우 escape 를 지원해주며 "%" 처럼 와일드카드 패턴을 만들 필요도 없음
        return StringUtils.hasText(itemName) ? item.itemName.contains(itemName) : null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) { // 반환 타입도 Predicate 를 사용하는 것 보다 BooleanExpression 를 추천
//        if (maxPrice != null) {
//            return item.price.loe(maxPrice);
//        }
        return maxPrice != null ? item.price.loe(maxPrice) : null;
    }
}
