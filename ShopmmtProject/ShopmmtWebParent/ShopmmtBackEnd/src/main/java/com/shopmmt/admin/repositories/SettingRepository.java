package com.shopmmt.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;

@Repository("settingRepository")
public interface SettingRepository extends JpaRepository<Setting, String> {
	
	List<Setting> findByCategory(SettingCategory category);
}
