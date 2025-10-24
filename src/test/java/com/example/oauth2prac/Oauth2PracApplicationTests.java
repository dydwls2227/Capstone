package com.example.oauth2prac;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("oauth") // 테스트 실행 시 "oauth" 프로필을 활성화합니다.
@SpringBootTest
class Oauth2PracApplicationTests {

    @Test
    void contextLoads() {
    }

}
