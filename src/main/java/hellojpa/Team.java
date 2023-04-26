package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    /**
     * 객체와 테이블간에 연관관계를 매즌 차이를 이해해야함
     *
     * 객체 연관관계 = 2개
     * 회원 -> 팀 연관관계 1개 (단방향)
     * 팀 -> 회원 연관관계 1개 (단방향)
     *
     * 테이블 연관관계 = 1개
     * 회원 <-> 팀의 연관관계 1개 (양방향) = pk = fk.
     * 테이블은 외래 키 하나로 두 테이블의 연관관계를 관리
     * MEMBER.TEAM_ID 외래 키 하나로 양방향 연관관계 가짐
     * (양쪽으로 조인할 수 있다.)
     *
     */
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        member.setTeam(this);
        members.add(member);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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
