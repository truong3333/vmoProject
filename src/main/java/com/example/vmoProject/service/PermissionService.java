package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.PermissionRequest;
import com.example.vmoProject.domain.dto.response.PermissionResponse;
import com.example.vmoProject.domain.entity.Permission;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;

    public PermissionResponse create(PermissionRequest request){
        if(permissionRepository.existsByName(request.getName())){
            log.error("Permission: {} already exists, create failed.");
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = new Permission(request.getName(), request.getDescription());

        permissionRepository.save(permission);
        log.info("Permission: {} create successfully",permission.getName());

        return PermissionResponse.builder()
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

    public List<PermissionResponse> getAll(){
        List<Permission> permissions = permissionRepository.findAll();

        if(permissions.isEmpty()){
            log.error("No permission found in the system,getAll permission failed.");
            throw new AppException(ErrorCode.PERMISSION_NULL);
        }

        log.info("Get all permission successfully.");

        return permissions.stream().map(permission -> {
            return PermissionResponse.builder()
                    .name(permission.getName())
                    .description(permission.getDescription())
                    .build();
        }).collect(Collectors.toList());
    }

//    public PermissionResponse update(String name,PermissionRequest request){
//        Permission permission = permissionRepository.findById(name).orElseThrow(() -> {
//            log.error("Permission with name: {} not found, update Permission failed!", name);
//            throw new RuntimeException("Permission not found");
//        });
//
//        permission.setName(request.getName());
//        permission.setDescription(request.getDescription());
//
//        permissionRepository.save(permission);
//
//        return PermissionResponse.builder()
//                .name(permission.getName())
//                .description(permission.getDescription())
//                .build();
//    }

    public void delete(String name){
        if(!permissionRepository.existsByName(name)){
            log.error("Permission not found, delete failed.");
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(name);
        log.info("Permission delete successfully.");
    }


}
