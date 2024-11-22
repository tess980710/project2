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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final ReserRepo reserRepo;

    private final StoreRepo storeRepo;
    private final HttpSession session;

    public String signUp(UserDto dto, String confirmPassword) {
        if (reserRepo.existsById(dto.getId())) {
            return "1";
        }
        if (!dto.getPassword().equals(confirmPassword)) {
            return "2";
        } else {
            reserRepo.save(dto);
            return "3";
        }
    }

    public List<UserDto> MasterList(Integer role) {
        return reserRepo.findByRole(role);
    }

    public void MasterModify(UserDto userDto) {
        UserDto dto = (UserDto) session.getAttribute("user");

        dto.setRole(userDto.getRole());
        reserRepo.save(dto);
    }

    public void write(StoreDto storeDto,MultipartFile file) {
        String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
        UUID uuid = UUID.randomUUID();

        String filename = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(uploadDir, filename);
//        String uniqueFile = UUID.randomUUID() + "_" + filename;


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

    public void modify(StoreDto storeDto, MultipartFile file)  {
        UserDto dto = (UserDto) session.getAttribute("user");


        storeDto.setUpdatedtime(LocalDate.now());

        storeDto.setUserid(dto.getId());
        UUID uuid = UUID.randomUUID();

        if (storeDto.getPhoto() != null && !storeDto.getPhoto().isEmpty()) {

            String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";
            String fullPath = uploadDir + "\\" + storeDto.getPhoto();

            File oldFile = new File(fullPath);

            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        if (file == null && file.isEmpty()) {
            String uploadDir = "C:\\practice\\project2\\src\\main\\resources\\static";

            String filename = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(uploadDir, filename);
        }

        storeRepo.save(storeDto);
    }
}

