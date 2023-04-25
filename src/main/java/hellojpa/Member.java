package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Entity 사용 시
 * JPA가 관리
 * @Entity(name = defalut = class name
 */
@Entity     // jpa를 사용확인
public class Member {

    @Id
    private Long id;

    @Column(unique = true, length = 10)
    private String name;

    public Member() {}

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
