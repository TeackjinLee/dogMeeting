package com.springboot.dogmeeting.api.test;

import com.springboot.dogmeeting.datasource.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test")
    public List<Member> getAllMembers() {
        List<Member> memberList = testService.getAllMembers();
        return memberList;
    }

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){ // 뷰로 데이터를 넘겨 주는 모델 객체
        Person examPerson = new Person();
        examPerson.setId(1L);
        examPerson.setName("홍길동");
        examPerson.setAge(11);
        examPerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examPerson); // Person 객체 저장
        model.addAttribute("today", LocalDate.now());

        return "example"; // example.html라는 뷰 조회
    }

    @Getter
    @Setter
    class Person{
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
