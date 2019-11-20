package com.hanpass.image.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.hanpass.image.dto.ServerResponse;

public interface BgImageService {
	public List<Long> getBgImageSeqList(String userId);
	
	public ResponseEntity<byte[]> getImage(Long fileSeq);

	public ServerResponse registrationImage(List<MultipartFile> fileList) throws Exception;
	
	public ServerResponse deleteImage(Long fileSeq);
}
