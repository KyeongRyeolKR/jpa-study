package relationmapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // 단방향 매핑 - Member에도 연관관계가 매핑 되어 있기 때문에 논리적으로 양방향으로 됨
    // 연관관계의 주인이 아니기 때문에 읽기 전용이다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    // 양방향 관계에서는 toString()과 같이(ex:lombok, JSON 생성 라이브러리) 무한 루프가 될 수 있는 상황을 조심해야한다!!
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }
}
