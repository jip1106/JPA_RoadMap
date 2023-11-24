package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //도메인 클래스 컨버터가 중간에 동작
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        System.out.println("member = " + member);
        return member.getUsername();
    }

    //페이징
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5, sort = "username",direction = Sort.Direction.DESC) Pageable pageable){
        //요청 파라미터를 page, size, sort 등 넣을 수 있음
        Page<Member> page = memberRepository.findAll(pageable);

        //Entity를 바로 내보내면 안됌
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        /*
        return memberRepository.findAll(pageable)
                .map(member -> new MemberDto(member.getId(),member.getUsername(), null))
         */

        /*
        return memberRepository.findAll(pageable)
                .map(MemberDto :: new);

        */

        return map;
    }

    


    @PostConstruct
    public void init(){
        for(int i=0; i<100; i++)
            memberRepository.save(new Member("user" + i,10 + i));
    }

}
