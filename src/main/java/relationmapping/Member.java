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
    // 단방향 연관관계 매핑 - Team에도 연관관계가 매핑 되어 있기 때문에 논리적으로 양방향으로 됨
    // 연관관계의 주인 - 무조건 외래키를 가진 엔티티가 주인이 된다.(즉, N인 엔티티가 주인)
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

    // 연관관계 편의 메서드
    // 단순히 값만 변경되는 것이 아닌, 로직이 있기 때문에 메서드 네이밍 변경(setTeam -> changeTeam)
    // 애플리케이션 상황에 따라 반대로 Team 엔티티에서도 이러한 편의 메서드를 만들어서 사용할 수도 있다.
    // 하지만 절대로 편의 메서드를 양쪽에 두개를 만들어서 사용하면 안되니 꼭! 어느 쪽에 만들지 정하고 한 쪽에서만 사용하도록!!!
    public void changeTeam(Team team) {
        this.team = team;

        // 양방향으로 변경되는 값을 세팅하기 위해 추가한 코드
        team.getMembers().add(this);
    }

//        public Long getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }

    // 양방향 관계에서는 toString()과 같이(ex:lombok, JSON 생성 라이브러리) 무한 루프가 될 수 있는 상황을 조심해야한다!!
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", team=" + team +
                '}';
    }
}
