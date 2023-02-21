package com.ll.hogamapp.base.initData;

import com.ll.hogamapp.bounded_context.accounts.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProdInitData {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            AccountService accountService
    ) {
        return args -> {
            if (initDataDone) {
                return;
            }

            initDataDone = true;

            accountService.whenSocialLogin("KAKAO", "KAKAO_2674471835", "홍길동");
        };
    }
}
