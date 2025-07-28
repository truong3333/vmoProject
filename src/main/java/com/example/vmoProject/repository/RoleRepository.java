package com.example.vmoProject.repository;

import com.example.vmoProject.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    boolean existsByName(String name);
}
