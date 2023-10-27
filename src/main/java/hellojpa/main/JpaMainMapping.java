package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class JpaMainMapping {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            //member.setTeamId(team.getId()); //객체지향 스럽지 않음,, setTeam
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            //teamId로 엮여 있어서 계속 찾아내야 하는 문제점
            //객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.
            //테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
            //테이블과 객체 사이에는 이런 큰 간격이 있다.
            Member findMember = em.find(Member.class , member.getId());

            List<Member> members = findMember.getTeam().getMembers();
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            //Long findTeamId = findMember.getTeamId();
            //em.find(Team.class,findTeamId);

            Team findTeam = findMember.getTeam();

            System.out.println("findTeam = " + findTeam.getName());


            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();


    }
}
