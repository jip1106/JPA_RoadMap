package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //JPA에서 데이터를 변경할땐 트랜잭션이 있어야 함
@RequiredArgsConstructor
public class MemberService {
    /*
    @Autowired
    private MemberRepository memberRepository;
    */

    //setter 인젝션
    /*
    private MemberRepository memberRepository;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
     */

    //생성자 인젝션
    private final MemberRepository memberRepository;
/*
    //롬복으로 대체 (RequiredArgsConstructor)
    @Autowired  //생성자가 하나일땐 Autowired 생략 가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

 */


    //회원 가입
    @Transactional(readOnly = false)
    public Long join(Member member){
        validateDuplicateMember(member);    //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){

        //return memberRepository.findOne(memberId);
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name) {
        //Member findMember = memberRepository.findOne(id);   //db에서 찾아와서 영속상태
        Member findMember = memberRepository.findById(id).get();
        findMember.setName(name);       //변경감지에 의해 update문 실행
    }
}
