package hellojpa.main;

import hellojpa.Member;
import hellojpa.Member3;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class JpaJPQLMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member3 member3 = new Member3();
            member3.setUsername("kimaaa");

            em.persist(member3);

            Member3 member4 = new Member3();
            member4.setUsername("kim");
            em.persist(member4);

            String jpql = "select m from MEMBER3 m where m.username like '%kim%'";
            List<Member3> resultList = em.createQuery(
                    jpql,
                    Member3.class
            ).getResultList();


            for (Member3 m : resultList) {
                System.out.println("m = " + m.getUsername());
            }


            //Criteria 사용 준비
            /*
            * Criteria
            *  - 문자가 아닌 자바 코드로 JPQL을 작성
            *  - JPQL 빌더 역할
            *  - JPA 공식 기능
            *
            *  - 복잡하고 실용성이 없음
            *
            *  - Criteria 보다 QueryDSL 사용 권장
             * */
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member3> query = cb.createQuery(Member3.class);

            Root<Member3> m = query.from(Member3.class);

            CriteriaQuery<Member3> cq = query.select(m).where(cb.equal(m.get("username"),"kim"));
            List<Member3> resultList1 = em.createQuery(cq).getResultList();
            System.out.println("resultList1 = " + resultList1.get(0));


            List resultList2 = em.createNativeQuery("Select member_id , city, street, zipcode, username From MEMBER3").getResultList();
            System.out.println("size :: " + resultList2.size());
            for(int i=0; i<resultList2.size();i++){
                Member3 tmp = (Member3)resultList2.get(i);

                if(resultList2.get(i) instanceof Member3){
                    System.out.println("Member3 instance");
                }

                if(tmp instanceof Member3){
                    System.out.println("tmp = " + tmp);
                    System.out.println(tmp.getUsername());
                }

                System.out.println("resultList2 = " + resultList2.get(i).getClass());
            }



            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();

    }
    
}
