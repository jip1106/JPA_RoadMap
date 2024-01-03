package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
//화면에서 자주 사용되는 복잡한 쿼리들은 따로 리포지토리 클래스를 만드는게 나은지 , Custom으로 만들지 고려 해 봐야함
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers(){
        return em.createQuery("select m From Member m ",Member.class).getResultList();
    }
}
