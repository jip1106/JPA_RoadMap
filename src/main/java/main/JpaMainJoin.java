package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMainJoin {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String joinQuery = "select m from Member m inner join m.team t";    //회원과 팀을 이너조인
            List<Member> query = em.createQuery(joinQuery, Member.class).getResultList();

            String leftOuterJoinQuery = "select m from Member m left join m.team t";    //회원과 팀을 이너조인
            List<Member> query2 = em.createQuery(leftOuterJoinQuery, Member.class).getResultList();

            String setaJoinQuery = "select m from Member m, Team t Where m.username = t.name";    //회원과 팀을 이너조인
            List<Member> query3 = em.createQuery(setaJoinQuery, Member.class).getResultList();
            System.out.println("query3.size() = " + query3.size());

            System.out.println("onJoinQuery===============");
            String onJoinQuery = "select m from Member m left join m.team t on t.name = 'member1'";
            List<Member> query4 = em.createQuery(onJoinQuery, Member.class).getResultList();

            System.out.println("onJoinQuery2===============");
            String onJoinQuery2 = "select m From Member m left join Team t on m.username = t.name";
            //String onJoinQuery2 = "select m From Member m join Team t on m.username = t.name";
            List<Member> query5 = em.createQuery(onJoinQuery2, Member.class).getResultList();



            tx.commit();

        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
