package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

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
             */
            Member member = new Member(200L, "member200");
            em.persist(member);

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
             * 쓰지 지연 SQL 저장소에 있는 SQL이 DB에 반영이 되는 과정
             * 
             * 플러시는 !
             * 영속성 컨텍스트를 비우지 않음
             * 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
             * 트랜잭션이라는 작업 단위가 중요 ! -> 커밋 직전에만 동기화 하면 됨
             */
            em.flush();

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

            System.out.println("=================================");


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
}