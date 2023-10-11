package relationmapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 양방향 매핑
 * - 양방향 매핑은 사실 설계 단계에서는 필요 없을 수 있다.
 *   단방향 매핑만으로도 이미 연관관계 매핑은 끝이 난 상태이다.
 *   단, 양방향 매핑을 함으로써 반대 방향으로도 조회를 할 수 있는 기능이 추가된 것 뿐이다.
 *   또한 실무에서는 JPQL에서 역방향 탐색할 일이 많다.
 *   그러므로 설계 단계에서는 단방향 매핑을 깔끔하게 설정하는 것이 중요하며,
 *   양방향 매핑은 테이블에 영향을 주지 않기 때문에 필요할 때 추가하면 된다!
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            // 저장
            Team team = new Team();
            team.setName("TeamA");
//            team.getMembers().add(member); // 연관관계의 주인이 아니기 때문에 변경이 불가능함
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team); // 연관관계의 주인이기 때문에 변경이 가능함
            em.persist(member);

//            team.getMembers().add(member); // 주인이 아님에도 값을 변경함 - 연관관계 편의 메서드(changeTeam())를 만듬으로써 생략 가능

            // 생성된 쿼리를 확인 및 DB 동기화를 하기 위해 강제로 flush 호출 및 영속성 컨텍스트 초기화
//            em.flush();
//            em.clear();

            // flush를 하지 않으면 1차 캐시에서 team을 찾아오는데, 그땐 팀에 저장된 멤버가 없다.
            // 그러므로 조회할 수 없다!
            // 그렇기 때문에 양방향 관계에서는 값을 둘 다 변경해야한다!!
            // 물론 JPA 입장에서 보면 주인에만 값을 변경해도 괜찮지만, 객체지향 입장으로 보면 둘 다 변경해야한다.
            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();
            System.out.println("=====================");
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }
            System.out.println("=====================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
