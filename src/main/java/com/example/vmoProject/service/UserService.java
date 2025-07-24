package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.response.UserResponseForAdmin;
import com.example.vmoProject.domain.entity.UserDetail;
import com.example.vmoProject.domain.dto.request.UserCreateRequest;
import com.example.vmoProject.domain.dto.request.UserUpdateRequest;
import com.example.vmoProject.domain.dto.response.UserResponse;
import com.example.vmoProject.domain.entity.User;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            log.error("User with username {} already exists, create failed!", request.getUsername());
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UserDetail userDetail = new UserDetail();
        userDetail.setEmail(request.getEmail());
        userDetail.setFullName(request.getFullName());
        userDetail.setPhone(request.getPhone());
        userDetail.setDob(request.getDob());
        userDetail.setGender(request.getGender());
        userDetail.setCMND(request.getCMND());
        userDetail.setAddress(request.getAddress());

        user.setUserDetail(userDetail);
        userDetail.setUser(user);

        userRepository.save(user);

        log.info("User {} created successfully!", user.getUsername());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .fullName(userDetail.getFullName())
                .build();
    }

    public List<UserResponseForAdmin> getAllUsers(){
        List<User> listAllUsers = new ArrayList<>(userRepository.findAll());
        if (listAllUsers.isEmpty()) {
            log.warn("No users found in the system.");
            return new ArrayList<>();
        }else{
            log.info("Admin has retrieved the list of all users");
        }
        return listAllUsers.stream()
                .map(user ->
                {
                    UserDetail userDetail = user.getUserDetail();
                    return UserResponseForAdmin.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .fullName(userDetail.getFullName())
                            .email(userDetail.getEmail())
                            .phone(userDetail.getPhone())
                            .CMND(userDetail.getCMND())
                            .address(userDetail.getAddress())
                            .gender(userDetail.getGender())
                            .dob(userDetail.getDob())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(String userId,UserUpdateRequest request){


        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID: {} not found, update User failed!", userId);
           return new RuntimeException("User not found");
        });

        UserDetail userDetail = user.getUserDetail();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userDetail.setEmail(request.getEmail());
        userDetail.setFullName(request.getFullName());
        userDetail.setPhone(request.getPhone());
        userDetail.setGender(request.getGender());
        userDetail.setDob(request.getDob());
        userDetail.setCMND(request.getCMND());
        userDetail.setAddress(request.getAddress());

        user.setUserDetail(userDetail);
        userDetail.setUser(user);

        userRepository.save(user);
        log.info("User with ID: {} updated successfully!", userId);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(userDetail.getEmail())
                .phone(userDetail.getPhone())
                .fullName(userDetail.getFullName())
                .build();
    }

    public void deleteUser(String userId){
        if (!userRepository.existsById(userId)) {
            log.error("User with ID: {} not found, delete User failed!", userId);
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

}
