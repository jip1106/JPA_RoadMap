package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
    }

    //JPQL
    @Test
    public void startJPQL(){
        //member1을 찾아라.
        String qlString = "Select m From Member m Where m.username = :username";

        Member findByJPQL = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findByJPQL.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
        //필드로 빼서 사용
        //JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //QMember m = new QMember("m");   //별칭 직접 지정    => 같은 테이블을 조인해야 하는 경우가 있으면 사용
        //QMember m = QMember.member; //기본 인스턴스 사용
        QMember m = member;     //static import 사용

        Member findByQueryDSL = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();


    assertThat(findByQueryDSL.getUsername()).isEqualTo("member1");
    }

    //검색조건 쿼리
    @Test
    public void search(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //Where 조건에 콤마로 이어가면 and 조건을 따라감 username = member1 and age = 10
    @Test
    public void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //결과 조회 (카운트)
    @Test
    public void resultFetch(){
        List<Member> fetch = queryFactory
                    .selectFrom(member)
                    .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();


        /* fetchResults 사용X*/
        /*
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        long totalCnt = results.getTotal();
        System.out.println("totalCnt = " + totalCnt);

        List<Member> content = results.getResults();
        */

        Long totalCount = queryFactory.select(member.count())
                .from(member)
                .fetchOne();


        Long totalCountWhere = queryFactory.select(member.count())
                .from(member)
                .where(member.age.between(10,30))
                .fetchOne();


        System.out.println("totalCount = " + totalCount);
        System.out.println("totalCountWhere = " + totalCountWhere);

    }


    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순
     * 2. 회원 이름 올림차순
     * 단 2에서 회원 이름이 없으면 마지막에 출력
     */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));


        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();


        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    /**
     * 페이징
     */
    @Test
    public void paging1(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
        assertThat(result.size()).isEqualTo(2);

    }

    /**
     * 집합
     */
    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구하라
     * @throws Exception
     */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();


        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /**
     * 기본 조인
     * 팀 A에 소속된 모든 회원
     */
    @Test
    public void join() throws Exception {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");
    }

    /**
     * 세타 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회
     * @throws Exception
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * join on 절 - JPA2.1 부터 지원
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     *
     * JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     * @throws Exception
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> teamA = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        /*
        //inner 조인인경우 on 이나 where 이나 상관 X

        queryFactory
                .select(member,team)
                .from(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();


        queryFactory
                .select(member,team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
          */

        for (Tuple tuple : teamA) {
            System.out.println("tuple = " + tuple);
        }
    }


    /**
     * 연관관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     * @throws Exception
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }


    @PersistenceUnit
    EntityManagerFactory emf;
    /**
     * 페치조인이 없을때
     * @throws Exception
     */
    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //로딩된 엔티티인지, 아닌지를 알려줌
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    /**
     * 페치조인 실행
     * @throws Exception
     */
    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        //로딩된 엔티티인지, 아닌지를 알려줌
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    /**
     * 서브쿼리
     * com.querydsl.jpa.JPAExpressions 사용
     *
     * 나이가 가장 많은 회원 조회
     * @throws Exception
     */
    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원
     * @throws Exception
     */
    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(30,40);
    }

    /**
     * in 서브쿼리
     * @throws Exception
     */
    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(20,30,40);
    }

    /**
     * select 서브쿼리
     * @throws Exception
     */
    @Test
    public void selectSubQuery() throws Exception {

        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void basicCase() throws Exception {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase() throws Exception {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /**
     * 상수
     * @throws Exception
     */
    @Test
    public void constant() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }


    @Test
    public void concat() throws Exception {
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sipleProjection() throws Exception {
        //프로젝션 대상이 하나(member.username)
        List<String> fetch = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println("s = " + s);
        }

        //프로젝션 대상이 하나(member.username)
        List<Member> fetch1 = queryFactory
                .select(member)
                .from(member)
                .fetch();

        for (Member member1 : fetch1) {
            System.out.println("member1 = " + member1);
        }
    }


    @Test
    public void tupleProjection() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            //Tuple로 조회 => 리포지토리단에서만 쓰고 서비스로 던질땐 DTO로 변환
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    //JPQL로 DTO 가져오기
    @Test
    public void findDtoByJPAL() throws Exception {
        //jpql은 new 연산자를 사용해서 생성자를 만들어줌
        /*
            순수 JPA에서 DTO를 조회할때는 new 명령어를 사용해야함
            DTO의 package 이름을 다 적어줘야해서 지저분함
            생성자 방식만 지원함
         */
        List<MemberDto> result = em.createQuery("select " +
                        " new study.querydsl.dto.MemberDto(m.username, m.age) " +
                        " from Member m", MemberDto.class).getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    //1. setter를 활용한 방법 -> getter, setter 필요 O
    @Test
    public void findDtoByQuerydslSetter() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    //2. field를 이용한 방법 -> getter, setter 필요 X
    @Test
    public void findDtoByQuerydslField() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    //3. 생성자를 이용한 방법
    @Test
    public void findDtoByQuerydslConstructor() throws Exception {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }



    //UserDTO 조회 => 필드가 username 이 아니고, name인경우 .as 사용
    @Test
    public void findUserDtoByQuerydslField() throws Exception {
        QMember subMember = new QMember("subMember");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),

                        ExpressionUtils.as(JPAExpressions
                                .select(subMember.age.max())
                                .from(subMember), "age"),       //subquery는 ExpressionUtils 로 감싸야함 max age 조회

                        member.age.as("realAge")

                        ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    public void findUserDtoByQuerydslConstruct() throws Exception {

        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    public void findDtoByQueryProjection() throws Exception {

        /*
            => MemberDto에 @QueryProjection 사용
         */
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();


        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }


    /*
        동적쿼리 -
         1. BooleanBuilder 사용
         2. Where 다중 파라미터 사용
     */

    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);

        assertThat(result.size()).isEqualTo(1);
    }

    //BooleanBuilder 사용
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();

        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }

        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    //Where 다중 파라미터 사용
    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);

        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {

        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    //수정,삭제 배치 쿼리
    @Test
    public void bulkUpdate() throws Exception {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
        
        //벌크연산 실행 후 영속성 컨텍스트 초기화
        em.flush();
        em.clear();
        
        /*
            db에 직접 쿼리를 실행한다고 해서 영속성 컨텍스트 상태가 변경되는건 아님
            member1 = 10 -> DB 비회원      영속성컨텍스트 : member1
            member2 = 20 -> DB 비회원      영속성컨텍스트 : member2
            member3 = 30 -> DB member3    영속성컨텍스트 : member3
            member4 = 40 -> DB member4    영속성컨텍스트 : member4
         */

        List<Member> result = queryFactory
                                .selectFrom(member)
                                .fetch();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void bulkAdd() throws Exception {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    //삭제
    @Test
    public void bulkDelete() throws Exception {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    @Test
    public void sqlFunction() throws Exception {
        List<String> result = queryFactory
                .select(
                        Expressions.stringTemplate(
                                "function('replace',{0},{1},{2})",
                                member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void sqlFunction2() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(
                        Expressions.stringTemplate("function('lower', {0})"
                                , member.username)))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

}
