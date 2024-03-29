package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainLoading {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);
            
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team);
            
            em.persist(member1);


            
            em.flush();
            em.clear();
            
            Member m = em.find(Member.class, member1.getId());

            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());

            System.out.println("==============");
            m.getTeam();
            m.getTeam().getName();  //초기화
            System.out.println("==============");
                    


            tx.commit();

        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();

    }
}
