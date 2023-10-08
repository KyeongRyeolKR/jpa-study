package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
// 시퀀스 오브젝트 직접 생성
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 50)
// 테이블 전략
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {

    /*
    기본 키 매핑(@Id, @GeneratedValue)
    - @Id : 내가 직접 id를 지정해줄 때 @Id만 사용
    - @GeneratedValue : 자동으로 생성시킬 때 사용
      -> 전략(strategy)
      - GenerationType.IDENTITY : 기본 키 생성을 위임(MySQL - AUTO_INCREMENT)
      - GenerationType.SEQUENCE : 시퀀스 오브젝트 생성 및 값 세팅
      - GenerationType.TABLE : 키 생성 전용 테이블을 하나 만들어서 DB 시퀀스를 흉내내는 전략(장점 : 모든 DB에 적용 가능, 단점 : 성능)
    - 권장하는 식별자 전략 : NOT NULL, UNIQUE, 절대 변하지 않는 값
     */
    @Id
    /*
    !주의!
    GerationType.IDENTITY 전략은 DB에 insert 쿼리가 나간 후에 ID 값이 정해진다.
    그러므로 처음 저장할 때, JPA 입장(영속성 컨텍스트)에서는 ID 값이 null로 들어가게 되므로
    SQL 저장소에 등록 후 한번에 보내는 것이 아니라 persist() 호출 시점에 바로 insert 쿼리를 보낸다.
     */
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*
    !주의!
    GenerationType.SEQUENCE 전략은 시퀀스를 먼저 조회해야 ID 값을 알 수 있으므로
    persist() 시점에 시퀀스를 조회해서 현재 ID 값을 얻어온다.
    IDENTITY 전략과 다르게 ID 값을 미리 알게 됨으로써 SQL 버퍼링이 가능하다!
     */
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
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
