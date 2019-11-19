package com.hanpass.image.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.hanpass.image.dto.DeleteImageResponse;
import com.hanpass.image.dto.RegImageResponse;

public interface BgImageService {
	public List<Long> getBgImageSeqList(String userId);
	
	public ResponseEntity<byte[]> getImage(Long fileSeq);

	public RegImageResponse registrationImage(List<MultipartFile> fileList) throws Exception;
	
	public DeleteImageResponse deleteImage(Long fileSeq);
}
