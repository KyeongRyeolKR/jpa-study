package relationmapping;

import javax.persistence.*;

/**
 * 상속관계 매핑
 * - 관계형 데이터 베이스는 상속 관계가 없다.
 * - 슈퍼타입 서브타입 관계라는 모델링 기법이 그나마 유사하다.
 * - 상속관계 매핑 : 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑
 *
 * 전략(Strategy)
 * - JOINED(정석!!)
 *   - 장점 : 데이터 정규화, 외래 키 참조 무결성 제약조건 활용 가능, 저장공간 효율적
 *   - 단점 : 조회 시 조인 사용으로 성능 저하, 조회 쿼리 복잡함, 데이터 저장 시 INSERT SQL 2번 호출
 * - SINGLE_TABLE
 *   - 장점 : 조인이 필요 없으므로 조회 성능 빠름, 조회 쿼리 단순함
 *   - 단점 : 자식 엔티티가 매핑한 컬럼은 모두 null 허용, 하나의 테이블에 모든것을 저장하므로 상황에 따라 조회 성능이 더 느려질 수 있음
 * - TABLE_PER_CLASS(사실상 쓰면 안되는 전략)
 *   - 장점 : 서브 타입을 명확하게 구분해서 처리할 때 효과적, not null 제약조건 사용 가능
 *   - 단점 : 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL), 자식 테이블을 통합해서 쿼리하기 어려움
 *
 * 단순하고 중요하지 않은 엔티티라면 단일 테이블 전략, 복잡하고 중요하다면 조인 전략을 사용해라.
 * 물론 상황에 맞는 트레이드오프를 고민해야한다!
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// 단일 테이블 전략에서는 필수로 사용해야한다!
// 구현 클래스마다 테이블 생성 전략에서는 아예 필요가 없으므로 DTYPE이 생성되지 않음!
@DiscriminatorColumn
public abstract class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}




