package com.example.project2.Controller;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.ReserRepo;
import com.example.project2.service.MasterService;
import com.example.project2.service.ReserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Group;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final ReserService reserService;

    private final ReserRepo reserRepo;

    private final HttpSession session;

    private final MasterService masterService;


    public String Index(Model model) {

        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("role", user.getRole());
        }
        return "Index";
    }

    @GetMapping("login")
    public String Login() {
        return "reservation/login";
    }

    @PostMapping("login")
    public String postLogin(@Validated @ModelAttribute UserDto dto) {
        boolean success = reserService.login(dto.getId(), dto.getPassword(), dto.getRole());
        if (success) {
            session.setAttribute("role", dto.getRole());

            return "redirect:/";
        } else {
            return "reservation/login";
        }
    }

    @PostMapping("logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("reservation")
    public String reservationList() {
        return "reservation/reservation";
    }

    @GetMapping("masterreservation")
    public String masterReservation() {
        return "reservation/masterreservation";
    }

    @GetMapping("storelist")
    public String storeList(Model model) {
        Integer role = (Integer) session.getAttribute("role");
        model.addAttribute("role", role);

        List<StoreDto> list = reserService.getAllStores();
        model.addAttribute("storeList", list);
        return "reservation/storelist";
    }


    @GetMapping("store/detail/{id}")
    public String StoreDetail(@PathVariable("id") Integer id, Model model) {
        StoreDto storeDto = reserService.getItemById(id);
        model.addAttribute("store", storeDto);
        return "reservation/storedetail";
    }
    @GetMapping("write")
    public String StoreWrite(){
        return "reservation/write";
    }

        @PostMapping("/write")
        public String postWrite( @ModelAttribute StoreDto storeDto, @RequestParam("file") MultipartFile file){
            masterService.write(storeDto,file);
           return "redirect:/storelist";
        }
    @PostMapping("/delete")
    public String detailDelete(@RequestParam("id") Integer id){
        reserService.deleteById(id);
        return "redirect:/storelist";
    }

    @GetMapping("/modify/{id}")
    public String modifyDetail(@PathVariable("id")Integer id,Model model){
        StoreDto dto = reserService.getItemById(id);
        model.addAttribute("store",dto);
        return"reservation/modify";

    }
    @PostMapping("/modify")
    public String MasterRoleUpdate(@Validated @ModelAttribute UserDto userDto) {
        masterService.MasterModify(userDto);

        UserDto userDto1 = (UserDto) session.getAttribute("user");

        return "redirect:/";
    }

}



