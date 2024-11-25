package com.example.project2.Controller;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.UserRepo;
import com.example.project2.service.MasterService;
import com.example.project2.service.ReserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private final UserRepo userRepo;

    private final HttpSession session;

    private final MasterService masterService;


    public String Index(Model model) { //메인페이지

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
    } //로그인 화면이동

    @PostMapping("login") // 로그인실행
    public String postLogin(@Validated @ModelAttribute UserDto dto) {
        boolean success = reserService.login(dto.getId(), dto.getPassword(), dto.getRole());
        if (success) {
            session.setAttribute("role", dto.getRole());

            return "redirect:/";
        } else {
            return "reservation/login";
        }
    }

    @PostMapping("logout") //로그아웃
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("reservation") //본인 예약리스트확인
    public String reservationList() {
        return "reservation/reservation";
    }

    @GetMapping("masterreservation") //점주, 관리자 예약자 확인
    public String masterReservation() {
        return "masterReser";
    }

    @GetMapping("storelist")//가게 모든 리스트
    public String storeList(Model model) {
        Integer role = (Integer) session.getAttribute("role");
        model.addAttribute("role", role);

        List<StoreDto> list = reserService.getAllStores();
        model.addAttribute("storeList", list);
        return "reservation/storelist";
    }


    @GetMapping("store/detail/{id}")//가게 상세정보
    public String StoreDetail(@PathVariable("id") Integer id, Model model) {
        StoreDto storeDto = reserService.getItemById(id);
        model.addAttribute("store", storeDto);
        return "reservation/storedetail";
    }
    @GetMapping("write")//가게 등록 페이지
    public String StoreWrite(){
        return "reservation/write";
    }

        @PostMapping("/write")//가게 등록 진행
        public String postWrite( @ModelAttribute StoreDto storeDto, @RequestParam("file") MultipartFile file){
            masterService.write(storeDto,file);
           return "redirect:/storelist";
        }
    @PostMapping("/delete")//가게 삭제
    public String detailDelete(@RequestParam("id") Integer id){
        reserService.deleteById(id);
        return "redirect:/storelist";
    }


    @GetMapping("/modify/{id}")// 가게 수정페이지
    public String modifyDetail(@PathVariable("id") Integer id, Model model) {
        StoreDto storeDto = reserService.getItemById(id);
        model.addAttribute("store", storeDto);
        return "reservation/modify";
    }

    @PostMapping("/modify")// 가게및 파일 삭제 후 수정진행
    public String modifyStore(
            @ModelAttribute StoreDto storeDto,
            @RequestParam("file") MultipartFile file,
            @RequestParam("existingPhoto") String existingPhoto) throws IOException {

        reserService.modifyStore(storeDto, file, existingPhoto);
        return "redirect:/storelist";
    }



    @GetMapping("/reservation/{id}")//예약 페이지 이동
    public String showReservationPage(@PathVariable("id") Integer storeId, Model model, HttpSession session) {
        StoreDto store = reserService.getItemById(storeId);
        UserDto user = (UserDto) session.getAttribute("user");
        model.addAttribute("store", store);
        model.addAttribute("user", user);
        return "reservation/storeReser";
    }

    @PostMapping("/reserve")//예약 진행
    public String reserveStore(
            @RequestParam("storeId") Integer storeId,
            @RequestParam("date") String date,
            HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        StoreDto store = reserService.getItemById(storeId);
        reserService.saveReservation(user, store, date);
        return "redirect:/reservation/storeReser";
    }

}



