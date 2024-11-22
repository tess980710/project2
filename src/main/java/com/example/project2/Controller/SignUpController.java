package com.example.project2.Controller;

import com.example.project2.dto.UserDto;
import com.example.project2.service.ReserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final ReserService reserService;

    @GetMapping
    public String signUp(){
        return "reservation/signup";
    }

    @PostMapping
    public String postSignUP(@ModelAttribute UserDto dto,
                             @RequestParam("confirmPassword") String confirmPassword,
                             Model model){
        String msg = reserService.signUp(dto,confirmPassword);

        if(msg.equals("1")){
            model.addAttribute("msg","아이디가 중복됩니다");
            return "reservation/signup";
        }else if(msg.equals("2")){
            model.addAttribute("msg2","비밀번호가 일치하지 않습니다");
            return "reservation/signup";
        }else{
            model.addAttribute("msg3","회원가입이 완료되었습니다");
        }
        return "reservation/login";
    }

}
