package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMainMapping2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            //team 은 읽기 전용 (mappedBy)
            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member);

            em.persist(team);
            */
            Team team = new Team();
            team.setName("TeamA");
            //team.getMembers().add(member);
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            //member.setTeam(team); //**
            //member.changeTeam(team);

            team.addMember(member);

            /*
             JPA 입장 에서는 주인 에만 세팅 해주면 되지만
             양방향 연관 관계시 양쪽 객체에 다 값을 넣어 주는게 좋다.

             */
            //메서드를 통해서 연관관계 편의 메소드를 생성하여 셋팅
            //team.getMembers().add(member); //**

            em.persist(member);

            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            System.out.println("members = " + findTeam);

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();


    }
}
