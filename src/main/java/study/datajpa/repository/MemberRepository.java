package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    //스프링 데이터 JPA는 특화된 검색 들은 어떻게 해결할까..? => 쿼리메소드 기능
    //findByUserName => 오류
    //List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    List<Member> findTop3By();

    long countMemberBy();

    boolean existsNameBy();
    boolean existsByUsername(String username);

    //named쿼리
    //@Query(name = "Member.findByUsername") //생략 가능
    List<Member> findByUsername(@Param("username") String username);

    //리포지토리 메서드에 쿼리 정의하기 jpql을 바로 적어줌
    @Query("Select m From Member m Where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //@Query로 단순한 값, DTO 조회하기
    //@Query로 단순한 값 조회하기
    @Query("select m.username From Member m")
    List<String> myFindUserNameList();

    //생성자로 만들어서 dto를 반환
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) From Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m From Member m Where m.username in :names")
    List<Member> findNameList(@Param("names") Collection<String> names);

    //반환타입을 유연하게 설정할 수 있음
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    //카운트 쿼리를 따로 가져올 수 있음
    @Query(value = "select m From Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findCountQueryByAge(int age, Pageable pageable);


    //벌크 수정 쿼리
    @Modifying(clearAutomatically = true)   //em.flush(), em.clear()
    @Query("update Member m set m.age = m.age + 1 Where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("Select m From Member m")
    List<Member> findMemberEntityGraph();

    //@EntityGraph(attributePaths = ("team"))
    @EntityGraph("Member.all") // Member 엔티티에 @NamedEntityGraph
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value="true"))
    Member findReadOnlyByUsername(String username);
    
    //jpa가 lock을 제공
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}


