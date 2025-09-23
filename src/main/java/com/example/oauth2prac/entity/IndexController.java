package com.example.oauth2prac.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/success")
    public String success(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            model.addAttribute("errorMessage", "로그인 정보가 없습니다.");
            return "error";
        }

        String name = "";
        String email = "";

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("sub")) {
            // Google 로그인 처리
            name = (String) attributes.get("name");
            email = (String) attributes.get("email");
        } else if (attributes.containsKey("id") && attributes.containsKey("kakao_account")) {
            // Kakao 로그인 처리
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");
            email = (String) kakaoAccount.get("email");
        }

        model.addAttribute("name", name);
        model.addAttribute("email", email);

        return "success";
    }

    @GetMapping("/error")
    public String error(Model model) {
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        }
        return "error";
    }
}
