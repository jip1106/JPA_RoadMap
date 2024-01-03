package hellojpa.main;

import hellojpa.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMainValueType {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("city", "street", "10000");

            Member3 member3 = new Member3();
            member3.setUsername("member1");
            member3.setHomeAddress(address);
            em.persist(member3);


            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            Member3 member4 = new Member3();
            member4.setUsername("member2");
            member4.setHomeAddress(copyAddress);
            em.persist(member4);

            //member4의 주소만 바꾸고 싶었지만 member4도 변경되어버림 -> 복사해서 사용
            // -> setter을 제거 하거나 private로 만들어서 불변객체로 설계 하면 된다
            //member4.getHomeAddress().setCity("newCity");






            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }

}
