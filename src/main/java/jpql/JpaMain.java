package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
 *
 * 페이징 API
 * - JPA는 페이징을 다음 두 API로 추상화함
 * - setFirstResult(int startPosition) : 조회 시작 위치 지정(0부터 시작)
 * - setMaxResult(int maxResult) : 조회할 데이터 수
 *
 * 조인
 * - 내부 조인 : SELECT m FROM Member m [INNER] JOIN m.team t
 * - 외부 조인 : SELECT m FROm Member m LEFT [OUTER] JOIN m.team t
 * - 세타 조인 : SELECT count(m) from Member m, Team t WHERE m.username = t.name
 *
 *   ON절
 *   - 조인 대상 필터링
 *     - ex) 회원과 팀을 조인하면서 팀 이름이 A인 팀만 조인
 *       -> JPQL : SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A'
 *          SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name = 'A'
 *   - 연관관계 없는 엔티티 외부 조인
 *     - ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
 *       -> JPQL : SELECT m, t FROM Member m LEFT JOIN Team t ON m.username = t.name
 *          SQL : SELECT m.* t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
 * 서브 쿼리
 *   지원 함수
 *   - [NOT] EXISTS (subquery) : 서브 쿼리에 결과가 존재하면 참
 *   - {ALL | ANY | SOME} (subquery)
 *     - ALL : 모두 만족하면 참
 *     - ANY, SOME : 조건을 하나라도 만족하면 참
 *   - [NOT] IN (subquery) : 서브 쿼리의 결과 중 하나라도 같은 것이 있으면 참
 *
 *   JPA 서브 쿼리 한계
 *   - JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
 *   - 하지만 하이버네이트를 구현체로 사용하면 SELECT 절도 사용 가능!
 *   - FROM 절의 서브 쿼리는 JPQL에서 불가능
 *     -> 해결 방안 : 조인으로 풀 수 있다면 풀어서 해결
 *
 * JPQL 타입 표현
 * - 문자 : 'HELLO', 'She''s'
 * - 숫자 : 10L, 10D, 10F
 * - Boolean : TRUE, FALSE
 * - ENUM : jpabook.MemberType.ADMIN (패키지명 포함)
 * - 엔티티 타입 : TYPE(m) = Member (상속 관계에서 사용)
 *
 *   기타(SQL과 문법이 같은 식)
 *   - EXISTS, IN
 *   - AND, OR, NOT
 *   - =, >, >=, <, <= ,<>
 *   - BETWEEN, LIKE, IS NULL
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', TRUE from Member m where m.type = :userType";
            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : resultList) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

