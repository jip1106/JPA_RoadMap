package study.querydsl.repository;

//스프링 데이터 JPA를 사용한 동적쿼리

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
    Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable);
}
