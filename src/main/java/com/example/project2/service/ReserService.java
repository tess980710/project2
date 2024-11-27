package com.example.project2.service;

import com.example.project2.dto.ReservationDto;
import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.ReserRepo;
import com.example.project2.repo.UserRepo;
import com.example.project2.repo.StoreRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserService {

    private final UserRepo userRepo;
    private final HttpSession session;

    private final ReserRepo reserRepo;
    private final StoreRepo storeRepo;

    private final JavaMailSender mailSender;

    private final EmailService emailService;
    public boolean login(String id, String password, Integer role) {
        Optional<UserDto> result = userRepo.findByIdAndPassword(id, password);
        if (result.isPresent()) {
            session.setAttribute("user", result.get());
            return true;
        } else {
            return false;
        }
    }

    public String signUp(UserDto dto, String confirmPassword) {
        if (userRepo.existsById(dto.getId())) {
            return "1";
        }
        if (!dto.getPassword().equals(confirmPassword)) {
            return "2";
        }
        userRepo.save(dto);
        return "3";
    }


    public List<StoreDto> getAllStores() {
        return storeRepo.findAll();

    }

    public StoreDto getItemById(Integer id) {
        StoreDto store = storeRepo.findById(id).orElse(null);
        return store;
    }

    public void deleteById(Integer id) {
        Optional<StoreDto> storeDto = storeRepo.findById(id);

        if (storeDto.isPresent()) {
            StoreDto store = storeDto.get();

            String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
            String photoPath = store.getPhoto();


            String fullPath = uploadDir + "\\" + photoPath;

            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            }

            storeRepo.deleteById(id);
        }
    }

    public void modifyStore(StoreDto storeDto, MultipartFile file, String existingPhoto) throws IOException {
        if (!file.isEmpty()) {
            String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
            String newPhotoName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File newFile = new File(uploadDir + "\\" + newPhotoName);
            file.transferTo(newFile);
            storeDto.setPhoto(newPhotoName);

            String oldPhotoPath = uploadDir + "\\" + existingPhoto;
            File oldFile = new File(oldPhotoPath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        } else {
            storeDto.setPhoto(existingPhoto);
        }

        storeRepo.findById(storeDto.getId()).ifPresent(existingStore -> {
            existingStore.setTitle(storeDto.getTitle());
            existingStore.setDescription(storeDto.getDescription());
            existingStore.setNum(storeDto.getNum());
            existingStore.setPhoto(storeDto.getPhoto());

            storeRepo.save(existingStore);
        });
    }

    public void saveReservation(UserDto user, StoreDto store, String date) {
        ReservationDto reservation = new ReservationDto();
        reservation.setUser(user);
        reservation.setStore(store);
        reservation.setDate(date);
        reservation.setName(user.getName());
        reservation.setPhone(Integer.valueOf(user.getPhonenum()));

        reserRepo.save(reservation);
        sendReservationEmail(user,store,date);

    }

    private void sendReservationEmail(UserDto user, StoreDto store, String date) {
        try {
            String to = "jbugh710@gmail.com";
            String subject = "새로운 예약이 접수되었습니다.";

            String content = """
                안녕하세요. <br>
                
                새로운 예약이 접수되었습니다. <br><br>
                
                예약자: %s<br>
                연락처: %s<br>
                예약 날짜: %s<br>
                가게: %s
                """.formatted(user.getName(), user.getPhonenum(), date, store.getTitle());

            emailService.sendEmail(to, subject, content);
        } catch (MessagingException e) {

            System.out.println("이메일 전송 중 오류 발생: " + e.getMessage());
        }
    }


    public List<ReservationDto> getReserList(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        if (user == null) {
            return new ArrayList<>();
        }

        Integer role = user.getRole() != null ? user.getRole() : 0;
        List<ReservationDto> reservations = new ArrayList<>();

        if (role == 3) {

            return reserRepo.findAll();
        } else if (role == 2) {
            List<StoreDto> stores = storeRepo.findByUserid(user.getId());
            if (stores != null && !stores.isEmpty()) {
                for (StoreDto store : stores) {
                    List<ReservationDto> storeReservations = reserRepo.findByStore(store);
                    if (storeReservations != null && !storeReservations.isEmpty()) {
                        reservations.addAll(storeReservations);
                    }
                }
            }
            return reservations;
        }

        return new ArrayList<>();
    }




    public ReservationDto getReservationById(Integer id) {
        Optional<ReservationDto> reservationOpt = reserRepo.findById(id);
        return reservationOpt.orElse(null);
    }


    public void deleteReser(Integer id){
        Optional<ReservationDto> reservationDto = reserRepo.findById(id);
        reserRepo.deleteById(id);
    }

    public List<ReservationDto> getUserReservations(UserDto user) {

        return reserRepo.findByUserId(user.getId());
    }


}



