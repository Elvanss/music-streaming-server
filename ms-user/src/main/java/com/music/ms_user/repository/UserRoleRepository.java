package com.music.ms_user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.ms_user.domain.entity.UserRole;
import java.util.List;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>{
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :userId")
    List<UserRole> findByUserId(UUID userId);
}
