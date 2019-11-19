package com.hanpass.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanpass.image.model.BgImage;

public interface BgImageRepository extends JpaRepository<BgImage, Long> {
	public List<BgImage> findByUserId(String userId);
}
