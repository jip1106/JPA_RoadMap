package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberB");
        Member savedMember = memberRepository.save(member);

        Optional<Member> optFindMember = memberRepository.findById(savedMember.getId());
        Member findMember = optFindMember.get();


        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);


    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy(){
        List<Member> result = memberRepository.findHelloBy();

        List<Member> resultTop3 = memberRepository.findTop3By();
    }

    @Test
    public void findCountBy(){
        long count = memberRepository.countMemberBy();
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void existsBy(){
        Member member = new Member("준일", 111);
        memberRepository.save(member);

        boolean result = memberRepository.existsNameBy();
        assertThat(result).isTrue();

        result = memberRepository.existsByUsername("준일");
        assertThat(result).isTrue();
    }

    @Test
    public void testNamedQuery(){
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findByUsername(member.getUsername());

        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member);
    }


    @Test
    public void testQueryRepository(){
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<Member> result = memberRepository.findUser("AAA",10);

        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findUserNameList(){
        Member member = new Member("AAA", 10);
        Member member1 = new Member("AAA", 20);
        memberRepository.save(member);
        memberRepository.save(member1);

        List<String> usernameList = memberRepository.myFindUserNameList();

        assertThat(usernameList.size()).isEqualTo(2);
    }


    @Test
    public void findMemberDto(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        Member member1 = new Member("member1" , 10, teamA);
        Member member2 = new Member("member2" , 20, teamA);
        Member member3 = new Member("member3" , 30, teamB);
        Member member4 = new Member("member4" , 40, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        for (MemberDto memberDto : memberRepository.findMemberDto()) {
            System.out.println("memberDto :: " + memberDto);
        }
    }


    @Test
    public void findNameList(){


        Member member1 = new Member("member1" , 10);
        Member member2 = new Member("member2" , 20);
        Member member3 = new Member("member3" , 30);
        Member member4 = new Member("member4" , 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<String> nameList = new ArrayList<>();
        nameList.add(member1.getUsername());
        nameList.add(member2.getUsername());
        nameList.add(member3.getUsername());


        List<String> list = Arrays.asList("member1", "member2");

        for (Member member : memberRepository.findNameList(nameList)) {
            System.out.println(member);
        }

    }

    @Test
    public void returnType(){
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 10);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //데이터가 없는경우 empty 컬렉션이 반환 (size == 0)
        List<Member> a = memberRepository.findListByUsername("AAA");

        //단건 조회인데 데이터가 없는경우 null 반환
        Member a2 = memberRepository.findMemberByUsername("AAA");
        a2 = memberRepository.findMemberByUsername("not found name");
        System.out.println("null return = " + a2);


        Optional<Member> a3 = memberRepository.findOptionalByUsername("AAA");
        a3 = memberRepository.findOptionalByUsername("not found name");
        System.out.println("Optional.empty return = " + a3);
        System.out.println("optional orElse = " + a3.orElse(null));


        System.out.println("a = " + a);
        System.out.println("a2 = " + a2);
        System.out.println("a3 = " + a3);
    }


    //스프링 data-jpa가 제공해주는 페이징
    @Test
    public void paging(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //DTO로 반환할때..!
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //long totalCount = memberRepositoryotalCount(age);
        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalCount = " + totalCount);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        System.out.println("============ Slice ============");
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);
        assertThat(content.size()).isEqualTo(3);
        //assertThat(slice.getTotalElements()).isEqualTo(6);
        assertThat(slice.getNumber()).isEqualTo(0);
        //assertThat(slice.getTotalPages()).isEqualTo(2);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 11);
        Member member3 = new Member("CCC", 12);
        Member member4 = new Member("DDD", 13);
        Member member5 = new Member("EEE", 14);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);


        int result = memberRepository.bulkAgePlus(12);
/*
        em.flush();
        em.clear();
*/
        List<Member> member5List = memberRepository.findByUsername("EEE");
        System.out.println("member5List.get(0) = " + member5List.get(0));

        assertThat(result).isEqualTo(3);
        
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        System.out.println("==== Team Save Start ====");
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        System.out.println("==== Team Save End  ====");


        System.out.println("==== Member Save Start ====");
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        System.out.println("==== Member Save End ====");

        em.flush();
        em.clear();

        //N + 1 문제 발생! 회원 1 조회 -> 연관된 Team 또 조회
        System.out.println("==== N + 1 문제 Start  @EntityGraph 로 해결 ====");
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); //Proxy
            System.out.println("member.getTeam().getName()  = " + member.getTeam().getName());
        }


        System.out.println("==== Fetch Join Start ====");
        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();
        for (Member member : memberFetchJoin) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); //Entity
            System.out.println("member.getTeam().getName()  = " + member.getTeam().getName());
        }

        //EntityGraph
        System.out.println("==== EntityGraph Join Start ====");
        members = memberRepository.findMemberEntityGraph();
        members = memberRepository.findEntityGraphByUsername("member1");
    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); //영속성 컨텍스트 1차캐시에 남아있는 상태
        em.clear(); //영속성 컨텍스트가 다 비워짐

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");  //@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value="true"))
        System.out.println("=========================================");

        em.flush(); //더티체킹
    }

    @Test
    public void lock(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); //영속성 컨텍스트 1차캐시에 남아있는 상태
        em.clear(); //영속성 컨텍스트가 다 비워짐

        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 10, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.username("m1")
                .and(MemberSpec.teamName("teamA"));

        List<Member> result = memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void queryByExample(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //Probe
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        System.out.println("result.size()  = " + result.size());

       // assertThat(result.get(0).getUsername()).isEqualTo("m1");

    }

    @Test
    public void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnlyDto> result = memberRepository.findProjectionsDtoByUsername("m1");
        for (UsernameOnlyDto usernameOnlyDto : result) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto.getUsername());
        }

        List<UsernameOnlyDto> result2 = memberRepository.findProjectionsDtoTypeByUsername("m1", UsernameOnlyDto.class);
        for (UsernameOnlyDto usernameOnlyDto : result2) {
            System.out.println("result2 = " + usernameOnlyDto.getUsername());
        }

        List<NestedClosedProjections> result3 = memberRepository.findProjectionsDtoTypeByUsername("m1", NestedClosedProjections.class);
        for (NestedClosedProjections nestedClosedProjections : result3) {
            String username = nestedClosedProjections.getUsername();
            System.out.println("username = " + username);

            String name = nestedClosedProjections.getTeam().getName();
            System.out.println("name = " + name);

        }
    }

    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result = memberRepository.findByNativeQuery("m1");
        System.out.println(result);

        Page<MemberProjection> result1 = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result1.getContent();

        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection getUsername = " + memberProjection.getUsername());
            System.out.println("memberProjection getTeamName = " + memberProjection.getTeamName());
        }

    }

}