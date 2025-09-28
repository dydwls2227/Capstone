package com.example.oauth2prac.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public String generateToken(@PathVariable Long userId){
        User user = userRepository.findById(userId).get();
        if(user==null){
            return "error";
        }

        String accessToken=jwtTokenProvider.createToken(user.getId().toString(),user.getRoleKey());

        return accessToken;
    }
}
