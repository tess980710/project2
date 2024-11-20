package com.example.project2.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @GetMapping
    public String Index(){
        return"Index";
    }

    @GetMapping("login")
    public String Login(){
        return "reservation/login";
    }

    @GetMapping("signup")
    public String signUp(){
        return "reservation/signup";
    }

    @GetMapping("master")
    public String masterSignUp(){
        return"reservation/master";
    }

    @GetMapping("masterlist")
    public String masterList(){
        return"reservation/masterlist";
    }

    @GetMapping("reservation")
    public String reservationList(){
        return "reservation/reservation";
    }
    @GetMapping("masterreservation")
    public String masterReservation(){
        return "reservation/masterreservation";
    }
    @GetMapping("storelist")
    public String storeList(){
        return "reservation/storelist";
    }

}
