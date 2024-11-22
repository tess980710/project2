package com.example.project2.service;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.ReserRepo;
import com.example.project2.repo.StoreRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserService {

    private final ReserRepo reserRepo;
    private final HttpSession session;

    private final StoreRepo storeRepo;

    public boolean login(String id, String password, Integer role){
       Optional<UserDto> result = reserRepo.findByIdAndPassword(id,password);
       if(result.isPresent()){
           session.setAttribute("user",result.get());
           return true;
       }else {
           return false;
        }
    }
        public String signUp(UserDto dto, String confirmPassword){
       if(reserRepo.existsById(dto.getId())){
            return "1";
            }
       if (!dto.getPassword().equals(confirmPassword)){
           return "2";
       }
       reserRepo.save(dto);
       return "3";
        }


        public List<StoreDto> getAllStores() {
            return storeRepo.findAll();

    }
        public StoreDto getItemById(Integer id){
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



}
