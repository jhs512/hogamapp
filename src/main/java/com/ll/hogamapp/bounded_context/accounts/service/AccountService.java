package com.ll.hogamapp.bounded_context.accounts.service;

import com.ll.hogamapp.bounded_context.member.entity.Member;
import com.ll.hogamapp.bounded_context.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
    private final MemberService memberService;

    @Transactional
    public Member whenSocialLogin(String oauthType, String username, String nickname) {
        Optional<Member> memberByUsername = memberService.getMemberByUsername(username);

        if (memberByUsername.isPresent()) {
            Member member = memberByUsername.get();

            member.updateWhenSocialLogin(nickname);

            return member;
        }

        return joinWithSocialLogin(oauthType, username, nickname);
    }

    private Member joinWithSocialLogin(String oauthType, String username, String nickname) {
        return memberService.join(oauthType, username, nickname);
    }
}
