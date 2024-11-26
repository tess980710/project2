package com.example.project2.service;

import com.example.project2.dto.StoreDto;
import com.example.project2.dto.UserDto;
import com.example.project2.repo.MasterRepo;
import com.example.project2.repo.UserRepo;
import com.example.project2.repo.StoreRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final UserRepo userRepo;

    private final StoreRepo storeRepo;
    private final HttpSession session;

    private final MasterRepo masterRepo;

    public String signUp(UserDto dto, String confirmPassword) {
        if (userRepo.existsById(dto.getId())) {
            return "1";
        }
        if (!dto.getPassword().equals(confirmPassword)) {
            return "2";
        } else {
            userRepo.save(dto);
            return "3";
        }
    }

    public List<UserDto> MasterList(Integer role) {
        return userRepo.findByRole(role);
    }


    public void write(StoreDto storeDto,MultipartFile file) {
        String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
        UUID uuid = UUID.randomUUID();

        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(uploadDir, filename);
        String uniqueFile = UUID.randomUUID() + "_" + filename;


        UserDto dto = (UserDto) session.getAttribute("user");
        storeDto.setCreatedtime(LocalDate.now());
        storeDto.setTitle(storeDto.getTitle());
        storeDto.setDescription(storeDto.getDescription());
        storeDto.setNum(storeDto.getNum());
        storeDto.setUserid(dto.getId());
        storeDto.setPhoto(filename);


//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        storeRepo.save(storeDto);
    }

    public void modify(StoreDto storeDto, MultipartFile file, String existingPhoto) {
        String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
        UUID uuid = UUID.randomUUID();


        if (existingPhoto != null && !existingPhoto.isEmpty()) {
            String fullPath = uploadDir + "\\" + existingPhoto;
            File oldFile = new File(fullPath);

            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        if (file != null && !file.isEmpty()) {
            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, filename);

            try {
                file.transferTo(saveFile);
                storeDto.setPhoto(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        storeDto.setUpdatedtime(LocalDate.now());
        UserDto dto = (UserDto) session.getAttribute("user");
        storeDto.setUserid(dto.getId());

        storeRepo.save(storeDto);
    }

    public UserDto getUserById(String id) {
        return masterRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public void MasterModify(UserDto userDto) {
        UserDto dto = (UserDto) session.getAttribute("user");

        if (dto == null || dto.getId().equals(userDto.getId())) {
            return;
        }

        UserDto existingUser = masterRepo.findById(userDto.getId())
                .orElse(null);

        if (existingUser != null) {
            existingUser.setRole(userDto.getRole());
            masterRepo.save(existingUser);
        }
    }


}

