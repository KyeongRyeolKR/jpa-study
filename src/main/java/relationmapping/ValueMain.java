package relationmapping;

/**
 * 값 타입
 * - 기본 값 타입
 *   - 자바 기본 타입(int, double)
 *   - 래퍼 클래스(Integer, Long)
 *   - String
 *   자바의 기본 타입은 절대 공유하면 안됨.
 *   기본 타입은 항상 값을 복사함.
 *   Integer 같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경되지 않음.
 *
 * - 임베디드 타입(복합 값 타입)
 *   - 새로운 값 타입을 직접 정의할 수 있음
 *   - JPA는 임베디드 타입이라 함
 *   - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함
 *   - int, String과 같은 값 타입(엔티티가 아님)
 *
 *   장점
 *   - 재사용성
 *   - 높은 응집도
 *   - 해당 값 타입만 사용하는 의미있는 메서드를 만들 수 있음
 *   - 임베디드 타입을 포함한 모든 값 타입은 값 타입을 소유한 엔티티에 생명주기를 의존함
 *
 *   임베디드 타입과 테이블 매핑
 *   - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다!!
 *   - 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능하다.
 *   - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많다.
 *
 *   만약 한 엔티티에서 같은 값 타입을 사용하면?
 *   - 컬럼 명이 중복되므로 @AttributeOverrides/@AttributeOverride로 속성을 재정의 해주어야 한다!
 */
public class ValueMain {

    public static void main(String[] args) {
        Integer a = 10;
        Integer b = a;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }
}
