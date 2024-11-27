package com.example.project2.Controller;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.MasterRepo;
import com.example.project2.service.MasterService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterController {
    private final MasterService masterService;

    private final MasterRepo masterRepo;

    private final HttpSession session;
    @GetMapping
    public String signUp(){
        return "reservation/master";
    }

    @PostMapping
    public String postSignUP(@ModelAttribute UserDto dto,
                             @RequestParam("confirmPassword") String confirmPassword,
                             Model model){
        String msg = masterService.signUp(dto,confirmPassword);

        if(msg.equals("1")){
            model.addAttribute("msg","아이디가 중복됩니다");
            return "reservation/master";
        }else if(msg.equals("2")){
            model.addAttribute("msg2","비밀번호가 일치하지 않습니다");
            return "reservation/master";
        }else{
            model.addAttribute("msg3","회원가입이 완료되었습니다");
        }
        return "redirect:/";
    }

    @GetMapping("masterlist/{role}")
    public String MasterList(@PathVariable("role") Integer role, Model model) {
        List<UserDto> list = masterService.MasterList(role);
        model.addAttribute("masterList", list);
        return "reservation/masterlist";
    }



}

