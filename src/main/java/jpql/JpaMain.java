package jpql;

import javax.persistence.*;
import java.util.List;

/**
 * JPQL 문법
 * - 엔티티와 속성은 대소문자 구분 O (Member, age 등)
 * - JPQL 키워드는 대소문자 구분 X (SELECT, FROM, where 등)
 * - 엔티티 이름 사용, 테이블 이름이 아님!
 * - 별칭은 필수!!(ex: Member as m), as는 생략 가능
 * - 기본적으로 표준 SQL 문법을 대부분 지원함
 *
 * TypeQuery, Query
 * - TypeQuery : 반환 타입이 명확할 때 사용
 *   -> ex) em.createQuery("SELECT m FROM member m", Member.class)
 * - Query : 반환 타입이 명확하지 않을 때 사용
 *   -> ex) em.createQuery("SELECT m.username, m.age FROM Member m")
 *
 * 결과 조회 API
 * - query.getResultList() : 결과가 하나 이상일 때!(리스트 반환)
 *   - 결과가 없으면 빈 리스트 반환
 * - query.getSingleResult() : 결과가 정확히 하나일 때!(단일 객체 반환)
 *   - 결과가 없으면 NoResultException, 둘 이상이면 NonUniqueResultException
 *
 * 파라미터 바인딩
 * - 이름 기준
 *   - ex) SELECT m FROM Member m WHERE m.username = :username
 *     ->  query.setParameter("username", usernameParam);
 * - 위치 기준
 *   - ex) SELECT m FROM Member m WHERE m.username = ?1git
 *     ->  query.setParameter(1, usernameParam);
 *
 * 프로젝션
 * - SELECT 절에 조회할 대상을 지정하는 것
 * - 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
 * - DISTINCT로 중복 제거 가능
 *
 *   여러 값 조회
 *   - SELECT m.username, m.age FROM Member m
 *     1. Query 타입으로 조회
 *     2. Object[] 타입으로 조회
 *     3. new 명령어로 조회
 *       - 단순 값을 DTO로 바로 조회
 *       - 패키지 명을 포함한 전체 클래스 명 입력
 *       - 순서와 타입이 일치하는 생성자 필요
 *       - ex) SELECT new jpql.MemberDTO(m.username, m.age) FROM Member m
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

