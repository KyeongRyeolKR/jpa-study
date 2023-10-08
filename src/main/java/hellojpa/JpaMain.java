package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * JPA CRUD
 * C : em.persist(객체)
 * R : em.find(객체타입, PK)
 * U : 객체.setXXX(변경값)
 * D : em.remove(객체)
 *
 * EntityManagerFactory 는 하나로 애플리케이션 전체 공유함
 * EntityManager 는 쓰레드간에 공유하면 안됨! (사용하고 버려야함)
 * JPA 의 모든 데이터 변경은 트랜잭션 안에서 실행
 *
 * 영속성 컨텍스트의 이점
 * - 1차 캐시
 *  -> 같은 엔티티 매니저에서만 캐시가 되기 때문에 사실상 큰 성능 이점이 있지는 않다.
 *     하지만, 만약 매우 복잡한 비즈니스 로직이 있다면 큰 성능 이점이 있을 수도 있다.
 *
 * - 동일성 보장
 *  -> 같은 객체를 조회할 때, 같은 트랜잭션 안에서는 동일한 객체를 보장한다.('==' 비교 시 true)
 *
 * - 트랜잭션을 지원하는 쓰기 지연
 *  -> insert SQL을 마치 버퍼처럼 쓰기 지연 SQL 저장소에 모아놨다가
 *     커밋하는 시점에 쌓여있던 SQL을 한번에 보낸다(flush).
 *
 * - 변경 감지(Dirty Checking)
 *  -> 1차 캐시에 저장되어 있는 스냅샷과 비교해서 변경이 있다면
 *     update SQL을 쓰기 지연 SQL 저장소에 저장한다.(remove도 동일)
 *
 * - 지연 로딩
 *
 * 플러시(flush)
 * - 순서 : 변경 감지 -> 수정된 엔티티 쓰기 지연 SQL 저장소 등록 -> 쌓인 SQL을 DB에 전송(등록,수정,삭제)
 * - 보통, 커밋 시점 또는 JPQL 쿼리를 실행할 때 자동으로 호출된다.
 * - 플러시는 영속성 컨텍스트를 비우는것이 아님!
 * - 영속성 컨텍스트의 변경내용을 DB에 동기화
 * - 트랜잭션이라는 작업 단위에서 커밋 직전에만 동기화 해주면 된다.
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("A");

            System.out.println("=================");
            em.persist(member);
            System.out.println("=================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
