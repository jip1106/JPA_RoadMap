package keymap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainPrimaryKey {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            //member.setId(1L);
            System.out.println("==============");

            em.persist(member);
            System.out.println(member.getId());
            System.out.println("==============");

            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }


        emf.close();

    }
}
