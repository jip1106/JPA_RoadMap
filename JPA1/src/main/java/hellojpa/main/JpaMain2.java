package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class JpaMain2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
/*
            Member member = em.find(Member.class,1L);
            printMemberAndTeam(member);

            printMember(member);
  */


            /*
            Member member = new Member();
            member.setUsername("hello");

            em.persist(member);

            em.flush();
            em.clear();

            //Member findMember = em.find(Member.class, member.getId());
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember.getClass() = " + findMember.getClass());
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getUsername());

*/
            Member member1 = new Member();
            member1.setUsername("test");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember.getClass() = " + refMember.getClass()); //Proxy



            //em.detach(refMember); //영속성 컨텍스트에서 refMember을 꺼냄
            //em.clear();
            //em.close(); //영속성 컨텍스트를 닫거나

             refMember.getUsername();
            // detach로 영속성컨텍스트에서 꺼내거나, clear() 으로 영속성 컨텍스트를 초기화 하거나, close로 영속성 컨텍스트를 닫으면
            // 꺼내올수 없기 때문에 LazyInitializationException : could not initialize proxy Exception 발생


            tx.commit();

        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();

    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member){
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team);
    }
}
