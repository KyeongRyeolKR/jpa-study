package relationmapping;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 테이블 기준 설계
//    @Column(name = "TEAM_ID")
//    private Long teamId;

    // 객체 기준 설계
    // 단방향 연관관계 매핑
    @ManyToOne // N : Member, 1 : Team
    @JoinColumn(name = "TEAM_ID") // 조인할 컬럼 지정
    private Team team;

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

//        public Long getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }
}
