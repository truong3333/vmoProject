package com.example.vmoProject.service;

import com.example.vmoProject.domain.dto.request.RoleRequest;
import com.example.vmoProject.domain.dto.response.PermissionResponse;
import com.example.vmoProject.domain.dto.response.RoleResponse;
import com.example.vmoProject.domain.entity.Role;
import com.example.vmoProject.exception.AppException;
import com.example.vmoProject.exception.ErrorCode;
import com.example.vmoProject.repository.PermissionRepository;
import com.example.vmoProject.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public String create(RoleRequest request){
        if(roleRepository.existsByName(request.getName())){
            log.error("Role name: {} already exists, create failed!",request.getName());
            throw  new AppException(ErrorCode.ROLE_EXISTED);
        }

        var permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissions()));
        Role role = new Role();

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(new HashSet<>(permissions));

        roleRepository.save(role);
        log.info("Role {} create successfully!", role.getName());

        return "Role "+role.getName()+" create successfully!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll(){
        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty()){
            log.error("No role found in the system,getAll role failed.");
            throw new AppException(ErrorCode.ROLE_NULL);
        }

        log.info("Get all Role successfully.");
        return roles.stream().map(role -> {

            return RoleResponse.builder()
                    .name(role.getName())
                    .description(role.getDescription())
                    .permissions(role.getPermissions().stream().map(permission ->
                                    PermissionResponse.builder()
                                            .name(permission.getName())
                                            .description(permission.getDescription())
                                            .build()
                            ).collect(Collectors.toSet()))
                    .build();
        }).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @CacheEvict(value = "roles", allEntries = true)
    public void delete(String name){
        if(!roleRepository.existsByName(name)){
            log.error("Role not found, delete failed!");
            throw  new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(name);
        log.info("Role delete successfully!");

    }


}
