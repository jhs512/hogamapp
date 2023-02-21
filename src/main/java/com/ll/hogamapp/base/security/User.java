package com.ll.hogamapp.base.security;

import com.ll.hogamapp.bounded_context.member.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends org.springframework.security.core.userdetails.User implements OAuth2User {
    public static User from(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));

        if (member.hasPhoneNo()) {
            authorities.add(new SimpleGrantedAuthority("canUsePhoneNo"));
        }

        return new User(member, authorities);
    }

    public static void updateAuth(Member member) {
        User user = from(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, member.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    public boolean hasAuthority(String authorityName) {
        return getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authorityName));
    }
}
