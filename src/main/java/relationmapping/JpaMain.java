package relationmapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 양방향 매핑
 * - 양방향 매핑은 사실 설계 단계에서는 필요 없을 수 있다.
 *   단방향 매핑만으로도 이미 연관관계 매핑은 끝이 난 상태이다.
 *   단, 양방향 매핑을 함으로써 반대 방향으로도 조회를 할 수 있는 기능이 추가된 것 뿐이다.
 *   또한 실무에서는 JPQL에서 역방향 탐색할 일이 많다.
 *   그러므로 설계 단계에서는 단방향 매핑을 깔끔하게 설정하는 것이 중요하며,
 *   양방향 매핑은 테이블에 영향을 주지 않기 때문에 필요할 때 추가하면 된다!
 *
 * 일대다 [1:N]
 * - 일대다 단방향은 일(1)이 연관관계의 주인이다.
 *   테이블 일대다 관계는 항상 다(N) 쪽에 외래 키가 있다.
 *   그런데 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조가 된다.
 *   '@JoinColumn'을 꼭 사용해야한다! 그렇지 않으면 조인 테이블 방식(중간 테이블)을 사용한다.
 *
 *   [일대다 단방향 매핑의 단점]
 *   - 엔티티가 관리하는 외래 키가 다른 테이블에 있으며, 연관관계 관리를 위해 추가로 UPDATE SQL을 실행한다.
 *     그러므로 일대다 단방향 매핑보다는 차라리 다대일 양방향 매핑을 사용하는 것이 좋다.
 *
 *   [일대다 양방향]
 *   - 이러한 매핑은 공식적으로는 존재하지 않는다. 대신 @JoinColumn의 옵션으로 (insertable=false, updatable=false)을 넣어서
 *     읽기 전용 필드로 만들어 마치 양방향 매핑처럼 사용할 수 있다.
 *     하지만 권장하는 방식이 아니므로 이 역시 다대일 양방향을 사용하는 것이 좋다.
 *
 * 일대일 [1:1]
 * - 주 테이블이나 대상 테이블 중에 외래 키를 선택할 수 있다.
 *   외래 키에 데이터베이스 유니크(UNI) 제약 조건 추가를 해주면 된다.
 *   다대일 단방향 매핑과 굉장히 유사하다!
 *   일대일 양방향 매핑도 마찬가지로 다대일 양방향 매핑과 유사하다.
 *
 *   [주 테이블 외래 키 설정]
 *   - 장점 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
 *     단점 : 값이 없으면 외래 키에 null 허용
 *
 *   [대상 테이블 외래 키 설정]
 *   - 장점 : 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
 *     단점 : 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨
 *
 * 다대다 [N:N]
 * - 관계형 DB는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
 *   그러므로 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야한다.
 *   하지만 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계가 가능하다!
 *
 *   [다대다 매핑의 한계]
 *   - 다대다 매핑이 편리해보이지만 실무에서 사용하면 절대 안된다!!!
 *     연결 테이블이 단순히 연결만 하고 끝나지 않기 때문이다.
 *     ex) 주문시간, 수량 같은 데이터가 들어올 수 있음
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member1 = new Member();
            member1.setUsername("hello");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("hello");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member1.getId());
            System.out.println("m = " + m.getClass());

            Member mRef = em.getReference(Member.class, member1.getId());
            System.out.println("mRef = " + mRef.getClass());

            System.out.println("m == mRef : " + (m == mRef));

            Member m1 = em.find(Member.class, member1.getId());
//            Member m2 = em.find(Member.class, member2.getId());
            Member m2 = em.getReference(Member.class, member2.getId());
            System.out.println("m1 == m2 : " + (m1.getClass() == m2.getClass()));

//            Member findMember = em.find(Member.class, member1.getId());
            Member findMember = em.getReference(Member.class, member1.getId()); // 프록시

            System.out.println("findMember = " + findMember.getClass());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.username = " + findMember.getUsername());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
