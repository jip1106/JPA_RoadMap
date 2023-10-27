package jpabook.japshop;

import jpabook.japshop.domain.Order;
import jpabook.japshop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Order order = new Order();
            order.addOrderItem(new OrderItem());

            tx.commit();

        }catch(Exception ex){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();

    }
}
