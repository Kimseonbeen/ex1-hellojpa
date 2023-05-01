package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;
    /**
     * 일대일 양방향 관계 설정 시
     */
//    @OneToOne(mappedBy = "locker")
//    private Member member;
}
