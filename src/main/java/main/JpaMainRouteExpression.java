package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaMainRouteExpression {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);




            em.flush();
            em.clear();

            String query = "select m.username From Member m";
            //query = "select m.team From Member m";        //묵시적 내부조인 발생 -> Member과 Team을 조인해서 가져옴
            // em.createQuery(query, Team.class).getResultList();
            //query = "select m.team.name From Member m";
            //query = "select t.members From Team t";


            System.out.println("=== query ===");
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            query = "select t.members From Team t";
            System.out.println("=== query2 ===");
            List<Collection> resultList = em.createQuery(query, Collection.class).getResultList();

            System.out.println(resultList);


            query = "select m.username From Team t join t.members m";   //명시적 조인을 사용하자..!

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
