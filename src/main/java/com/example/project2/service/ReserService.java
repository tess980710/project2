package com.example.project2.service;

import com.example.project2.dto.ReservationDto;
import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.ReserRepo;
import com.example.project2.repo.UserRepo;
import com.example.project2.repo.StoreRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

    }

    public List<ReservationDto> getReserList(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        if (user == null) {

            return new ArrayList<>();
        }


        Integer role = user.getRole() != null ? user.getRole() : 0;

        if (role == 3) {
            return reserRepo.findAll();
        } else if (role == 2) {

            StoreDto store = storeRepo.findByUserid(user.getId());
            if (store != null) {
                return reserRepo.findByStore(store);
            }
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



