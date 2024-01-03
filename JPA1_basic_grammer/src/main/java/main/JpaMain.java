package main;

import jpql.Address;
import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            member = new Member();
            member.setUsername("member2");
            member.setAge(30);
            em.persist(member);

            TypedQuery<Member> query = em.createQuery("select m From Member m ", Member.class);
            query.getResultList();
            //query.getSingleResult();        //결과가 하나일 때만 사용

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m From Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            String teamQuery = "select m.team From Member m";
            teamQuery = "Select t From Member m join m.team t"; //조인을 명시적으로 적는게 좋음

            List<Team> resultTeam = em.createQuery(teamQuery, Team.class)
                    .getResultList();

            String orderQuery = "Select o.address From Order o";
            List<Address> addressResults = em.createQuery(orderQuery, Address.class).getResultList();

            String distinctQuery = "Select distinct m.username, m.age From Member m";
            List resultList = em.createQuery(distinctQuery).getResultList();

            Object o = resultList.get(0);
            Object[] resultObjList = (Object[])o;
            System.out.println("resultObjList[0] = " + resultObjList[0]);
            System.out.println("resultObjList[1] = " + resultObjList[1]);

            List<Object[]> resultList2 = em.createQuery(distinctQuery).getResultList();
            Object[] resultObj2 = resultList2.get(0);

            System.out.println("resultObj2[0] = " + resultObj2[0]);
            System.out.println("resultObj2[1] = " + resultObj2[1]);

            System.out.println("resultList2.size() = " + resultList2.size());
            resultObj2 = resultList2.get(1);
            System.out.println("resultObj2[0] = " + resultObj2[0]);
            System.out.println("resultObj2[1] = " + resultObj2[1]);



            String dtoQuery = "select new jpql.MemberDTO(m.username, m.age) from Member m";

            List<MemberDTO> dtoResultList = em.createQuery(dtoQuery, MemberDTO.class).getResultList();
            MemberDTO memberDTO = dtoResultList.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());





            /*
            Team team = new Team();
            team.setName("team1");
            em.persist(team);


            for(int i=0; i<2; i++){
                Member member = new Member();
                member.setUsername("member" + (i+1));
                member.setTeam(team);
                team.getMembers().add(member);
                em.persist(member);
            }

             */


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
