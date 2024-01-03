package study.datajpa.repository;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import study.datajpa.entity.Member;

public class MemberSpec {
    public static Specification<Member> teamName(final String teamName){
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                if(StringUtils.isEmpty(teamName)){
                    return null;
                }
                Join<Object, Object> team = root.join("team", JoinType.INNER);  //회원과 조인
                return criteriaBuilder.equal(team.get("name"), teamName);
            }
        };
    }


    public static Specification<Member> username(final String username){
        return (Specification<Member>) (root, query, builder) ->
                builder.equal(root.get("username"), username);

    }

}
