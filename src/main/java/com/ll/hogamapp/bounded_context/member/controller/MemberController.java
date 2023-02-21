package com.ll.hogamapp.bounded_context.member.controller;

import com.ll.hogamapp.base.rq.Rq;
import com.ll.hogamapp.base.security.User;
import com.ll.hogamapp.bounded_context.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String showEdit() {
        return "usr/member/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid MemberEditRequest req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getFieldError().getDefaultMessage());
        }

        memberService.edit(rq.getMember(), req.phoneNo);
        User.updateAuth(rq.getMember());

        return Rq.redirectWithMsg("/", "수정되었습니다.");
    }

    @AllArgsConstructor
    public static class MemberEditRequest {
        @NotBlank
        private String phoneNo;
    }
}
