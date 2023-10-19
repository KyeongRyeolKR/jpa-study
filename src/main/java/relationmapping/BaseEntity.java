package relationmapping;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * '@MappedSuperclass'
 * - 상속관계 매핑이 아님
 * - 엔티티 아님, 테이블과 매핑 안함
 * - 대신 상속 받은 자식 클래스에 매핑 정보만 제공함
 * - 조회, 검색 불가능 -> em.find(BaseEntity.class) 안됨!
 * - 직접 생성해서 사용할 일이 없으므로 추상 클래스 사용 권장
 *
 * 1) 테이블과 관계가 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
 * 2) 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
 * 참고 : @Entity 클래스는 엔티티 or @MappedSuperclass 로 지정된 클래스만 상속 가능!
 */
@MappedSuperclass // 매핑 정보만 받는 부모 클래스
public abstract class BaseEntity {

    private String createdBy;

    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
