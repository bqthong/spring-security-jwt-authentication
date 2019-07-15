package com.spring.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.model.RoleModel;
import com.spring.app.model.RoleModel.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long>{
    Optional<RoleModel> findByName(RoleName roleName);
}
