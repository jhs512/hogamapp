package com.ll.hogamapp.bounded_context.member.entity;

import com.ll.hogamapp.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username;
    private String password;
    private String nickname;
    private String oauthType;
    private String phoneNo;

    public void updateWhenSocialLogin(String nickname) {
        this.nickname = nickname;
    }

    public boolean hasPhoneNo() {
        return phoneNo != null;
    }

    public void updatePhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
