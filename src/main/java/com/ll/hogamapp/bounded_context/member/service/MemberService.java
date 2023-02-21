package com.ll.hogamapp.bounded_context.member.service;

import com.ll.hogamapp.bounded_context.member.entity.Member;
import com.ll.hogamapp.bounded_context.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username);
    }

    @Transactional
    public Member join(String oauthType, String username, String email, String nickname) {
        Member member = Member.builder()
                .oauthType(oauthType)
                .username(username)
                .password("")
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return member;
    }
}
