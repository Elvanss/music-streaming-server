package com.music.ms_user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.ms_user.domain.entity.UserSetting;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {
    
}
