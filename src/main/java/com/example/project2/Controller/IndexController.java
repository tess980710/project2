package com.example.project2.Controller;

import com.example.project2.dto.ReservationDto;
import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.UserRepo;
import com.example.project2.service.EmailService;
import com.example.project2.service.MasterService;
import com.example.project2.service.ReserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final EmailService emailService;

    // 메인 페이지
    public String Index(Model model) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userId", user.getId());
            model.addAttribute("role", user.getRole());
        }
        return "Index";
    }

    // 로그인 페이지 이동
    @GetMapping("login")
    public String Login() {
        return "reservation/login";
    }

    // 로그인 실행
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

    // 로그아웃
    @PostMapping("logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 점주/관리자 예약자 확인 페이지
    @GetMapping("masterreservation")
    public String masterReservation() {
        return "masterReser";
    }

    // 가게 목록 조회
    @GetMapping("storelist")
    public String storeList(Model model) {
        Integer role = (Integer) session.getAttribute("role");
        model.addAttribute("role", role);

        List<StoreDto> list = reserService.getAllStores();
        model.addAttribute("storeList", list);
        return "reservation/storelist";
    }

    // 가게 상세 정보 조회
    @GetMapping("store/detail/{id}")
    public String StoreDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        StoreDto storeDto = reserService.getItemById(id);
        model.addAttribute("store", storeDto);

        UserDto currentUser = (UserDto) session.getAttribute("user");

        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "reservation/storedetail";
    }


    // 가게 등록 페이지 이동
    @GetMapping("write")
    public String StoreWrite() {
        return "reservation/write";
    }

    // 가게 등록
    @PostMapping("/write")
    public String postWrite(@ModelAttribute StoreDto storeDto, @RequestParam("file") MultipartFile file) {
        masterService.write(storeDto, file);
        return "redirect:/storelist";
    }

    // 가게 삭제
    @PostMapping("/delete")
    public String detailDelete(@RequestParam("id") Integer id) {
        reserService.deleteById(id);
        return "redirect:/storelist";
    }

    // 가게 수정 페이지
    @GetMapping("/modify/{id}")
    public String modifyDetail(@PathVariable("id") Integer id, Model model) {
        StoreDto storeDto = reserService.getItemById(id);
        model.addAttribute("store", storeDto);
        return "reservation/modify";
    }

    // 가게 수정
    @PostMapping("/modify")
    public String modifyStore(@ModelAttribute StoreDto storeDto,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("existingPhoto") String existingPhoto) throws IOException {
        reserService.modifyStore(storeDto, file, existingPhoto);
        return "redirect:/storelist";
    }

    // 예약 페이지 이동
    @GetMapping("/reservation/{id}")
    public String showReservationPage(@PathVariable("id") Integer storeId, Model model, HttpSession session) {
        StoreDto store = reserService.getItemById(storeId);
        UserDto user = (UserDto) session.getAttribute("user");
        model.addAttribute("store", store);
        model.addAttribute("user", user);
        return "reservation/storeReser";
    }

    // 예약 진행
    @PostMapping("/reserve")
    public String reserveStore(@RequestParam("storeId") Integer storeId,
                               @RequestParam("date") String date,
                               HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        StoreDto store = reserService.getItemById(storeId);

        reserService.saveReservation(user, store, date);

        return "redirect:/storelist";
    }


    // 예약 리스트 조회
    @GetMapping("/storereserlist")
    public String showReservationList(Model model, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        List<ReservationDto> reservationList = reserService.getReserList(session);
        model.addAttribute("reservationList", reservationList);
        return "reservation/storeReserList";
    }

    // 내 예약 조회
    @GetMapping("/reservation")
    public String getMyReservations(HttpSession session, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("user");

        if (loggedInUser != null) {
            List<ReservationDto> reservations = reserService.getUserReservations(loggedInUser);
            model.addAttribute("reservations", reservations);
            return "reservation/Myreservation";
        }

        return "redirect:/login";
    }

    
    //내 예약 상세 리스트
    @GetMapping("/reservation/StoreDetailReser/{id}")
    public String showReservationDetail(@PathVariable Integer id, Model model, HttpSession session) {

        ReservationDto reservation = reserService.getReservationById(id);
        UserDto user = (UserDto) session.getAttribute("user");


        model.addAttribute("reservation", reservation);
        model.addAttribute("user", user);

        return "reservation/StoreDetailReser";
    }

    @PostMapping("/deleteReser")
    public String deleteReser(@RequestParam("id") Integer id, HttpSession session) {
        UserDto loggedInUser = (UserDto) session.getAttribute("user");
        if (loggedInUser != null && (loggedInUser.getRole() == 2 || loggedInUser.getRole() == 3)) {
            reserService.deleteReser(id);
            return "redirect:/storereserlist";
        } else {
            reserService.deleteReser(id);
            return "redirect:/reservation";
        }
    }

    @PostMapping("/master/update")
    public String updateRoles(
            @RequestParam("id") List<String> id,
            @RequestParam("role") List<Integer> role,
            RedirectAttributes redirectAttributes) {

        try {
            for (int i = 0; i < id.size(); i++) {
                UserDto user = new UserDto();
                user.setId(id.get(i));
                user.setRole(role.get(i));

                masterService.MasterModify(user);
            }

            redirectAttributes.addFlashAttribute("message", "권한 수정이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "권한 수정 중 오류가 발생했습니다.");
        }

        return "redirect:/";
    }

}
