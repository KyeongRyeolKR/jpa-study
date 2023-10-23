package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
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
 *
 * 조건식 - CASE 식
 * - 기본 CASE 식, 단순 CASE 식 : 기존 SQL 문법과 같음
 * - COALESCE : 하나씩 조회해서 null이 아니면 반환
 *   -> ex) 사용자 이름이 없으면 이름 없는 회원을 반환
 *      -> SELECT COALESCE(m.username, '이름 없는 회원') FROM Member m
 * - NULLIF : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
 *   -> ex) 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름을 반환
 *     -> SELECT NULLIF(m.username, '관리자') FROM Member m
 *
 * JPQL 기본 함수
 * - CONCAT, SUBSTRING, TRIM, LOWER, UPPER, LENGTH, LOCATE, ABS, SQRT, MOD
 * - SIZE : JPA 전용 함수, 컬렉션의 크기를 반환
 * - INDEX : JPA 전용 함수, @OrderColumn를 쓸 때만 사용 가능, 컬렉션의 위치 값 반환(웬만하면 안쓰는게 좋음)
 *
 * 참고 : 하이버네이트 구현체를 사용하면 위에 나열된 함수를 제외하고도 기본적으로 설정된 DB 방언에 따른 함수들이 미리 등록되어 있다.
 *
 * 사용자 정의 함수 호출
 * - 하이버네이트는 사용 전 방언에 추가해야 한다.
 * - 사용하는 DB 방언을 상속 받고 사용자 정의 함수를 등록한다.
 *
 * 경로 표현식
 * - .(점)을 찍어 객체 그래프를 탐색하는 것
 *
 *   용어 정리 및 특징
 *   - 상태 필드 : 단순히 값을 저장하기 위한 필드
 *     - 특징 : 경로 탐색의 끝, 더 탐색 불가
 *   - 연관 필드 : 연관관계를 위한 필드
 *     - 단일 값 연관 필드 : @ManyToOne, @OneToOne, 대상이 엔티티(ex: m.team)
 *       - 특징 : 묵시적 내부 조인 발생, 더 탐색 가능
 *     - 컬렉션 값 연관 필드 : @OneToMany, @ManyToMany, 대상이 컬렉션(ex: m.orders)
 *       - 특징 : 묵시적 내부 조인 발생, 더 탐색 불가
 *               하지만 FROM 절에서 명시적 조인을 사용해 별칭을 얻으면 별칭을 통해 탐색 가능!
 *
 *   명시적 조인, 묵시적 조인
 *   - 명시적 조인 : JOIN 키워드 직접 사용
 *   - 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
 *
 *   예시
 *   - SELECT o.member.team FROM Order o                    => 성공
 *     - 단일 값 연관 필드, 묵시적 조인 발생
 *   - SELECT t.members FROM Team                           => 성공
 *     - 컬렉션 값 연관 필드, 묵시적 조인 발생
 *   - SELECT t.members.username FROM Team t                => 실패
 *     - 컬렉션 값 연관 필드, 더이상 탐색이 불가능하기 때문에 실패
 *   - SELECT m.username FROM Team t JOIN t.members m       => 성공
 *     - 컬렉션 값 연관 필드를 더 탐색하기 위해 컬렉션 값 연관 필드를 조인을 해서 별칭을 얻고,
 *       상태 필드를 프로젝션으로 두어 명시적 조인으로 풀어냈기에 성공
 *
 *   묵시적 조인 시 주의 사항
 *   - 항상 내부 조인
 *   - 컬렉션은 경로 탐색의 끝! 명시적 조인을 통해 별칭을 얻어야함
 *   - 경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만, 묵시적 조인으로 인해 SQL의 FROM 절에 영향을 줌
 *
 *   실무 조언
 *   - 가급적 묵시적 조인 대신에 명시적 조인을 사용해라!
 *   - 조인은 SQL 튜닝에 굉장히 중요한 포인트이기 때문에
 *     묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어렵기 때문이다.
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
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m.username from Team t join t.members m";
            String result = em.createQuery(query, String.class)
                    .getSingleResult();

            System.out.println("result = " + result);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

