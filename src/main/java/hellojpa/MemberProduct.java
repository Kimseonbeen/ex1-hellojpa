package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct {
    /**
     * ManyToMany 사용하지 않고
     * 중간테이블을 Entity로 승격하여 
     * 다대다 한계 극복
     * MEMBER_ID
     * PRODUCT_ID 두개를 묶어서 PK로 사용하지 말고,
     * @GeneratedValue 사용한 의미없는 값 MEMBERPRODUCT_ID을 PK로 사용하는 것이 확장성이 유연하다.
     */

    @Id @GeneratedValue
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
