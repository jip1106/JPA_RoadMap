package main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMainBulkQuery {
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

            //em.flush();
            //em.clear();

            String query = "";

            query = "Update Member m Set m.age = 20";

            //FLUSH
            int resultCount = em.createQuery(query)
                    .executeUpdate();

            em.clear(); //영속성 컨텍스트를 초기화 해줘야 데이터 정합성 보장

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember);

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
