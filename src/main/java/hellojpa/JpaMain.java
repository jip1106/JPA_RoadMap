package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /* 데이터 저장 */
            Member member = null;
            for(long i=1; i<10; i++){
                //데이터 찾기 em.find
                Optional<Member> findMember = Optional.ofNullable(em.find(Member.class, i));

                if(findMember.isPresent()){
                       continue;
                }

                //비영속 상태
                member = new Member();
                member.setId(i);
                member.setName("박준"+ i);

                //영속 상태
                System.out.println("=== BEFORE ===");
                em.persist(member);

                System.out.println("=== AFTER ===");
            }

            //데이터 찾기
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = "  + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            //데이터 삭제
            em.remove(findMember);

            //데이터 수정
            findMember = em.find(Member.class, 2L);
            findMember.setName("박준삼");


            // Member 객체를 대상으로 셀렉
            List<Member> result = em.createQuery("Select m From Member as m", Member.class)
                    .getResultList();

            for (Member tmpMember : result) {
                System.out.println("member.name = " + tmpMember.getName());
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
