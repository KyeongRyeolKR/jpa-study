package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    /*
    @Column 옵션
    - updatable, insertable : 등록, 변경 가능 여부
    - nullable : null 값 허용 여부(DDL - NOT NULL)
    - unique : 유니크 제약 조건 설정, 하지만 이름이 복잡하게 설정되므로 잘 사용하지 않음(대신 @Table 의 uniqueConstrains 옵션 선호)
    - length : 문자 데이터 길이 지정(String 타입에서만 사용)
    - columnDefinition : DB 컬럼 정보 직접 지정
    - precision, scale : 아주 큰 숫자나 소숫점을 사용할 때 사용
     */
    @Column(name = "name", nullable = false) // DB 상 컬럼 명 : name
    private String username;

    private Integer age;

    /*
    @Enumerated 옵션
    - EnumType.ORDINAL : 기본 값, 순서 설정(이넘 타입에 새로운 값이 추가될 경우, 순서가 바뀌게 되므로 잘 사용하지 않는 옵션)
    - EnumType.STRING : 이넘의 문자 값 설정(권장하는 설정)
     */
    @Enumerated(EnumType.STRING) // 이넘 타입 매핑
    private RoleType roleType;

    /*
    @Temporal 옵션
    - TemporalType.DATE, TemporalType.TIME, TemporalType.TIMESTAMP
    - 해당 옵션은 java 8 버전에서 LocalDate, LocalDateTime이 추가 되어 굳이 사용하지 않는 옵션이 됐다.
     */
    @Temporal(TemporalType.TIMESTAMP) // 시간 타입 매핑(DATE, TIME, TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    /*
    @Lob 옵션
    - 필드 타입이 문자면 CLOB 타입, 나머지는 BLOB 매핑
    - CLOB : String, char[], java.sql.CLOB
    - BLOB : byte[], java.sql.BLOB
     */
    @Lob // 데이터가 큰 경우 사용
    private String description;

    /*
    @Transient 옵션
    - 필드 매핑을 하고 싶지 않을 때 사용
    - 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용
     */
    @Transient // DB에 반영하지 않고 메모리에서 사용하고 싶을 때 사용
    private int temp;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
