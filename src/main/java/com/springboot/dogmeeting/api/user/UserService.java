package com.springboot.dogmeeting.api.user;

import com.springboot.dogmeeting.api.common.config.jwt.JwtProperties;
import com.springboot.dogmeeting.api.common.config.jwt.TokenProvider;
import com.springboot.dogmeeting.api.user.dto.UserRequest.*;
import com.springboot.dogmeeting.datasource.user.User;
import com.springboot.dogmeeting.datasource.user.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    public Long save(AddUserDto dto) {
//
//        User user = userRepository.save(User.builder()
//                .email(dto.getEmail())
//                // 패스워드 암호화
//                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
//                .build());
//
//        String token = tokenProvider.generateToken(user, Duration.ofDays(14));
//
//        return Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .get("id", Long.class);
//    }

    public Long save(AddUserDto dto){
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // 패스워드 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("unexpected user"));
    }

}
