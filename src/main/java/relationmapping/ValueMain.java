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
 */
public class ValueMain {

    public static void main(String[] args) {
        Integer a = 10;
        Integer b = a;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }
}
