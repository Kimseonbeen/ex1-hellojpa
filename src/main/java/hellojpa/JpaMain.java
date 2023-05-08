package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            em.flush();
            em.clear();

            /**
             * em.find 인해 영속성 컨텍스트 안에 Member가 들어감
             * 그 이후 getReference 사용한다고 하더라도 프록시 객체를 반환하지 않음
             * 영속성 컨텍스트 안에 이미 존재하는데 굳이 프록시 객체를 반환할 이유가 없음
             * 성능상 이점이 없기 때문이다.
             *
             *             Member m1 = em.find(Member.class, member1.getId());
             *             System.out.println("m1.getClass() = " + m1.getClass());
             *
             *             Member reference = em.getReference(Member.class, member1.getId());
             *             System.out.println("reference.getClass() = " + reference.getClass());
             */

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("m1.getClass() = " + refMember.getClass());  //Proxy

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("reference.getClass() = " + findMember.getClass());  //Member

            // 하나의 트랜젝션 안에서는 == 비교는 JPA에서 무조건 true가 반환이 되어야 한다.!
            System.out.println("a == b : " + (refMember == findMember));    //true !
            
            // 프록시 인스턴스의 초기화 여부 확인
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
            // 강제초기화
            Hibernate.initialize(refMember);



            //
//            Member findMember = em.find(Member.class, member.getId());
            /**
             * ? select query 안나감
             * getReference 호출 시점에는 데이터베이스 조회를 하지 않음
             * 그런데, 이 값이 실제 사용되는 시점 밑에 sout에서 findMember.getUsername() 호출하는 시점에
             * JPA가 데이터베이스 조회를 실행하여 값을 채운다..
             * 
             * getReference() : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
             *
             * Member findMember = em.getReference(Member.class, member.getId());
             *             System.out.println("findMember = " + findMember.getClass());    // class hellojpa.Member$HibernateProxy$dPaHCZfb : 프록시 클래스
             *             System.out.println("findMember.getId() = " + findMember.getId());
             * getUsername 가져오는 시점에 실제 타겟에 대한 값을 알게된다.
             * System.out.println("findMember.getUsername() = " + findMember.getUsername());
             *
             */

//            Member member = em.find(Member.class, 1L);
//
//            printMember(member);
//
//            printMemberAndTeam(member);


            // em.persist(member);

            /**
             *             Team team = new Team();
             *             team.setName("TeamA");
             *             // team.getMembers().add(member);   주인이 아닌 경우 읽기만 가능하므로 add 하더라도 추가가 되지 않는다.
             *             em.persist(team);
             *
             *             Member member = new Member();
             *             member.setUsername("member1");
             *
             *             // 연관관계 편의 메서드
             *             team.addMember(member);
             *             em.persist(member);
             *
             * //            member.changeTeam(team);   //**
             */


            /**
             *  순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자
             *  team.getMembers().add(member);  //** 이 부분을 set 메서드에 추가 하면 
             *  setTeam으로 둘다 설정이 됨
             */
            /**
             * em.flush();
             * em.clear(); 를 하게되면 영속성 컨텍스트를 비우기 때문에
             * 다시 DB에서 조회해온다.
             * 그래서 tem.getId()의 값도 알수 있고, select 쿼리가 조회된다.
             */
//            em.flush();
//            em.clear();

 /*           Team findTeam = em.find(Team.class, team.getId());  //1차 캐시
            List<Member> members = findTeam.getMembers();

            System.out.println("================");
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }
            System.out.println("================");*/

            /**
             *
             * Member member1 = new Member();
             *             member1.setUsername("A");
             *
             *             Member member2 = new Member();
             *             member2.setUsername("B");
             *
             *             Member member3 = new Member();
             *             member3.setUsername("C");
             *
             *             System.out.println("===============");
             *             em.persist(member1);    //1, 51
             *             em.persist(member2);  //MEM
             *             em.persist(member3);  //MEM
             *             System.out.println("===============");
             */


            /**
             * 자바 컬렉션에 객체를 가져온것과 같이
             * JPA또한 같은 주소값을 가져옴
             * 가능한 이유는 1차캐시에서 가져왔기 때문에
             * System.out.println("result = " + (findMember1 == findMember2));
             * //            Member findMember1 = em.find(Member.class, 101L);
             * //            Member findMember2 = em.find(Member.class, 101L);
             */

            /**
             * 수정
             * 데이터를 찾아온 다음에 데이터를 변경 끝.
             * Member member = new Member(200L, "member200");
             * em.persist(member);
             */

            /**
             * 준영속상태
             * 영속성 컨텍스트에서 빠진경우를 준영속상태라고 한다.
             * 
             * Member member1 = em.find(Member.class, 150L);
             * member1.setName("AAAAA");
             *
             * em.detach(member1);
             * 
             * em.detach를 실행하는 경우 영속상태가 해제됌
             * 그래서 setName에 대한 업데이트 SQL은 DB에 보내지 않음
             */
          

            /**
             * 쓰기 지연 SQL 저장소에 있는 SQL이 DB에 반영이 되는 과정
             * 
             * 플러시는 !
             * 영속성 컨텍스트를 비우지 않음
             * 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
             * 트랜잭션이라는 작업 단위가 중요 ! -> 커밋 직전에만 동기화 하면 됨
             * em.flush();
             */

            // 중간에 JPQL 실행
            /**
             * 중간에 JPQL 실행하는 경우
             * JPQL 쿼리는 실행 시 플러시가 자동으로 호출 !
             *
             * ex) em.persist(memberA);
             * ex) em.persist(memberB);
             * ex) em.persist(memberC);
             * TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
             * List<Member> members = query.getResultList();
             *
             * 이 경우 방금 내용으론는 commit 시점에 flush가 되므로 list데이터가 없어야 하는데
             * 이 경우르 방지 하기 위해 jpql은 쿼리 실행 시 플러시가 자동으로 호출된다. !
             */

            // em.update(member)    이런 코드가 있어야 하지 않을까.. ? / 없어도 된다.


            // 객체를 저장한 상태 (영속)
            // 1차 캐시에 저장됨
            // 여기까지 INSERT SQL을 데이터베이스에 보내지 않음
            // em.persist(member);

            // 1차 캐시에서 조회
            // JPA는 데이터베이스를 바로 조회하는것이 아닌,
            // 1차캐시부터 확인을 한다.
            // Member findMember = em.find(Member.class, member.getId());

            /**
             * 시나리오 2
             * Member findMember = em.find(Member.class, "member2");
             * 영속성 컨테스트 안에 1차 캐시는 현재 id가 pk이고 값이 entity인
             * @id member1, Entity member1가 저장되어있다.
             * 위 로직에서 member2의 키를 찾을 경우
             * 1차캐시에 존재하지 않기 때문에, 1차캐시에 확인을하고 없을경우 -> DB조회 -> 1차 캐시에 저장 -> member22 반환이 이뤄진다.
             */

            // 커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();


    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2 : " + (m1 instanceof Member));
        System.out.println("m1 == m2 : " + (m2 instanceof Member));
    }

    private static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());


    }
}
