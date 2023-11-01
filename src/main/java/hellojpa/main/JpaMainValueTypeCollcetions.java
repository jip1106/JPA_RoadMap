package hellojpa.main;

import hellojpa.Address;
import hellojpa.AddressEntity;
import hellojpa.Member3;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMainValueTypeCollcetions {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member3 member = new Member3();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city","street","10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1","old_street","11000"));
            member.getAddressHistory().add(new AddressEntity("new_city","new_street","12000"));

            em.persist(member);

            em.flush();
            em.clear();
            System.out.println("============ START ============");
            Member3 findMember = em.find(Member3.class, member.getId());

            /*
            List<Address> addressHistory = findMember.getAddressHistory();

            for(Address address : addressHistory){
                System.out.println("address = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for(String favoriteFood : favoriteFoods){
                System.out.println("favoriteFood = " + favoriteFood);
            }

            System.out.println("============ 수정 START ============");
            Address beforeAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("변경",beforeAddress.getStreet(),beforeAddress.getZipcode()) );

            //치킨 -> 한식으로 바꾸고 싶을때
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            //주소 변경
            findMember.getAddressHistory().remove(new Address("old1","old_street","11000"));
            findMember.getAddressHistory().add(new Address("new111","old_street","11000"));
 */

            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }

}
