package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaMainFetchJoin {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select t from Team t";

            System.out.println("=== query ===");
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();


            for (Team team : result) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());

                for(Member member : team.getMembers()){
                    System.out.println(" - member = " + member);
                }
            }


            query = "select m from Member m Where m = :member";
            System.out.println("====== findMember ======");
            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember = " + findMember);

            System.out.println("====== findMember2 ======");
            query = "select m from Member m Where m.id = :memberId";

            findMember = em.createQuery(query,Member.class)
                            .setParameter("memberId",member1.getId())
                                    .getSingleResult();

            System.out.println("findMember2 = " + findMember);


            System.out.println("====== findMember3 ======");
            query = "select m From Member m Where m.team = :team";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("findMember3 = " + member);
            }


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
