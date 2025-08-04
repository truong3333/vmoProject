package com.example.vmoProject.repository;

import com.example.vmoProject.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String>{
    boolean existsByName(String name);
}
