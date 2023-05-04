package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Entity 사용 시
 * JPA가 관리
 * @Entity(name = default = class name
 */
@Entity     // jpa를 사용확인
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "member_seq",
        initialValue = 1, allocationSize = 50)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "name", nullable = false)    // nullable -> not null 제약조건
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    // @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) -> 일대다 양방향 설정 시 맵핑은 되어있으나, insert, update를 막는다. 읽기전용
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    public Team getTeam() {
        return team;
    }

    public Member() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
