package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.Member;

import java.util.List;

//Spring Data Jpa를 사용
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom , QuerydslPredicateExecutor<Member> {
    //인터페이스에 규칙에맞게 정의만하면 자동으로 매칭되서 만들어줌
    List<Member> findByUsername(String username);
}
