package com.springboot.dogmeeting.api.test;

import com.springboot.dogmeeting.datasource.member.Member;
import com.springboot.dogmeeting.datasource.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    MemberRepository memberRepository;  // 빈 주입

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
