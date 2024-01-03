package main;

import jpql.Address;
import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMainPaging {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(int i=0; i<100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }


            em.flush();
            em.clear();

            String query = "select m from Member m order by m.age desc";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
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
