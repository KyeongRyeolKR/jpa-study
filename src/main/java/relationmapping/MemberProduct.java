package relationmapping;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 다대다 관계를 일대다, 다대일 관계로 풀어내기 위한 엔티티
 * (사실상 해당 엔티티는 Order와 같은 느낌의 엔티티다.)
 */
//@Entity
public class MemberProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;
    private int price;
    private LocalDateTime orderDateTime;
}
