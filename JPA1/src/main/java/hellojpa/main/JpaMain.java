package hellojpa.main;

import hellojpa.Member;

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

            /**/
            /* 데이터 저장 */
            Member member = null;
            for(long i=1; i<10; i++){
                //데이터 찾기 em.find
                Optional<Member> findMember = Optional.ofNullable(em.find(Member.class, i));

                if(findMember.isPresent()){
                       continue;
                }

                //비영속 상태 -> JPA와 관계 없이 자바 객체 생성, 셋팅
                member = new Member();
                //@GeneratedValue 로 자동 생성
                //member.setId(i);
                member.setUsername("박준"+ i);

                //영속 상태
                //persist는 db에 저장하는게 아니라 엔티티를 영속성 컨텍스트에 저장하는것
                em.persist(member);
            }

            //데이터 찾기
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = "  + findMember.getId());
            System.out.println("findMember.name = " + findMember.getUsername());

            //데이터 삭제
            em.remove(findMember);

            //데이터 수정
            findMember = em.find(Member.class, 2L);
            findMember.setUsername("박준삼");


            // Member 객체를 대상으로 셀렉
            List<Member> result = em.createQuery("Select m From Member as m", Member.class)
                    .getResultList();

            for (Member tmpMember : result) {
                System.out.println("member.name = " + tmpMember.getUsername());
            }


            /*섹션3 영속성관리 - 내부동작방식*/
            //객체를 생성한 상태(비영속)
            Member member2 = new Member();
            long setIdVal = 100L;

            //member2.setId(setIdVal);
            member2.setUsername("HelloJPA");

            //객체를 저장한 상태 (영속)
            System.out.println("=== BEFORE ===");

            Optional<Member> optFindMember = Optional.ofNullable(em.find(Member.class, setIdVal));
            Optional<Member> optFindMember2 = Optional.ofNullable(em.find(Member.class, setIdVal));

            Member findMember1 = em.find(Member.class , setIdVal);
            Member findMember2 = em.find(Member.class , setIdVal);

            System.out.println("result = " + (findMember1 == findMember2));

            while(optFindMember.isPresent()){
                System.out.println("findMember.id = " + optFindMember.get().getId());
                System.out.println("findMember.name = " + optFindMember.get().getUsername());
                optFindMember = Optional.ofNullable(em.find(Member.class, ++setIdVal));
            }
            //member2.setId(setIdVal);
            em.persist(member2);
            System.out.println("=== AFTER ===");


            //회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
            //em.detach(member2);

            //객체를 삭제한 상태(삭제)
            //em.remove(member2);


            //Member m1 = new Member(150L, "A");
            //Member m2 = new Member(160L, "B");
            //em.persist(m1);
            //em.persist(m2);

            //
            // JPA는 commit 하는 시점에 내부적으로 flush()가 호출 되고, 영속성컨텍스트에
            // 엔티티와 스냅샷을 비교함
            // 값을 읽어온 최초 시점의 데이터를 스냅샷으로 떠놓고 엔티티와 스냅샷을 비교한다.
            // 엔티티가 수정된걸 찾아서 쓰기지연 SQL 저장소에 저장해놓고 commit 시점에 업데이트 처리
            //
            // JPA는 값을 바꾸면 트랜잭션이 커밋되는 시점에 업데이트 쿼리가 날라간다.
            //*
            Optional<Member> findMember150 = Optional.ofNullable(em.find(Member.class,150L));
            findMember150.ifPresent(value -> value.setUsername("수정한 이름"));

            //em.detach(findMember150);


            System.out.println("=================");
/*
            플러시 : 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
            변경감지 , 수정된 엔티티 쓰기 지연 SQL 저장소에 등록 , 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송(등록,수정,삭제 쿼리)
            영속성 컨텍스트를 플러시하는 방법
            1. em.flush(); 직접 호출
            2. 트랜잭션 커밋 - 플러시 자동 호출
            3. JPQL 쿼리 실행 - 플러시 자동 호출

            플러시는
             영속성 컨텍스트를 비우지 않음
             영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화
             트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됌
*/
            //Member member200 = new Member(200L,"member200");
            //em.persist(member200);

            //em.flush();
            System.out.println("============================");


            // 준영속 상태로 만드는 방법
            //    - em.detach(entity);
            //    - em.clear();
            //    - em.close();

            //트랜잭션을 커밋하는 시점에 영속성 컨텍스트에 있는 쿼리가 db에 날라감
            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();


    }
}
