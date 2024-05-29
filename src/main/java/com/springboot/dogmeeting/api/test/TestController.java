package com.springboot.dogmeeting.api.test;

import com.springboot.dogmeeting.datasource.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMembers() {
        List<Member> memberList = testService.getAllMembers();
        return memberList;
    }
}
