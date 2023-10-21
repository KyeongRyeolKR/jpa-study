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
 *
 *   객체 타입의 한계
 *   - 임베디드 타입 같은 객체 타입은 공유 참조를 (컴파일 타임에) 막을 수도 없고 피할 수도 없다.
 *
 *   객체 타입의 한계 극복
 *   - 불변 객체로 설계한다!
 *   - 가장 쉬운 방법 : 생성자로만 값을 설정하고 setter를 안만들면 됨.(또는 private setter만 만듬)
 *   - 참고 : Integer나 String이 자바의 대표적인 불변 객체이다.
 *
 *   그렇다면 객체 타입의 값을 바꾸고 싶은 상황에는 어떻게 하나?
 *   - 새로 객체를 만들어서 생성자에 바꾸고 싶은 값으로 넣어서 대입해야한다.
 *
 *   정리 : 깔끔하게 모든 값 타입은 불변 객체로 만들어야한다!!!!
 *
 * - 값 타입의 비교
 *   - 동일성(identity) 비교 : 인스턴스의 참조 값을 비교, == 사용
 *   - 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equals() 사용
 *   - 값 타입(임베디드/객체 타입)의 비교는 항상 equals()를 사용해서 비교해야한다.
 */
public class ValueMain {

    public static void main(String[] args) {
        int a = 10;
        int b = 10;
        System.out.println("a == b : " + (a == b));

        Address address1 = new Address("city", "street", "100");
        Address address2 = new Address("city", "street", "100");
        System.out.println("address1 == address2 : " + (address1 == address2));
        System.out.println("address1 equals address2 : " + (address1.equals(address2)));
    }
}
