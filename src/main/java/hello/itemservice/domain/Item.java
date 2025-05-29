package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "item") // 객체명과 같으면 생략 가능
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Column 을 생략 할 경우 필드 명을 칼럼 명으로 사용을 하는데, 기본적으로 카멜 케이스를 RDBMS 관례인 스네이크 케이스로 자동 변환을 지원
    @Column(name = "item_name", length = 10) // 고로, 이 부분을 작성하지 않아도 스네이크 케이스로 자동 변환
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
