package com.shopmmt.site.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;

@Repository("settingRepository")
public interface SettingRepository extends JpaRepository<Setting, String> {
	
	List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	List<Setting> findByTwoCategories(SettingCategory catOne, SettingCategory catTwo);

	Setting findByKey(String string);
}
