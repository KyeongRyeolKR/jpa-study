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
 * - 동일성 보장
 * - 트랜잭션을 지원하는 쓰기 지연
 * - 변경 감지
 * - 지연 로딩
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            // 비영속
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            System.out.println("=== AFTER ===");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
