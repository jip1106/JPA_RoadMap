package main;

import jpql.Member;
import jpql.MemberType;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;


public class JpaMainJPQLType {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//persistence.xml 의 <persistence-unit name="hello">

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);

            em.persist(member);

            //em.flush();
            //em.clear();

            String query = "select m.username, 'HELLO', TRUE From Member m ";
                    //query += "where m.memberType = jpql.MemberType.ADMIN";
                    query += "where m.memberType = :userType";
            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)         //파라미터 바인딩
                    .getResultList();

            for (Object[] objects : resultList) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[1] = " + objects[1]);
                System.out.println("objects[2] = " + objects[2]);
            }


            
            //case query
            
            query = "select " + 
                    "case when m.age <=10 then '학생요금' " +
                    "     when m.age >=60 then '경로요금' " +
                    "     else '일반요금' " +
                    "end " +
                    "From Member m";

            List<String> resultList1 = em.createQuery(query, String.class).getResultList();
            for (String s : resultList1) {
                System.out.println("s = " + s);
            }

            System.out.println("=============update query");
            //member.setUsername(null);
            System.out.println("=============update query");

            //coalesce
            query = "select coalesce(m.username, '이름 없는 회원') as username From Member m ";
            List<String> resultList2 = em.createQuery(query, String.class).getResultList();

            for (String s : resultList2) {
                System.out.println("s = " + s);
            }

            //nullif
            System.out.println("=============null if");
            query = "select nullif(m.username,'관리자') as username From Member m";
            List<String> resultList3 = em.createQuery(query, String.class).getResultList();
            for (String s : resultList3) {
                if(s == null){
                    System.out.println("nullif 함수 실행");
                }
                System.out.println("s = " + s);
            }


            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(20);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(30);
            em.persist(member3);

            query = "select function('group_concat',m.username) From Member m";
            List<String> resultList4 = em.createQuery(query, String.class).getResultList();

            System.out.println(resultList4.size());

            for (String s : resultList4) {
                System.out.println("resultList4 s = " + s);
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
