package com.hanpass.image.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.hanpass.image.dto.ServerResponse;
import com.hanpass.image.service.BgImageService;

@RestController
@CrossOrigin(origins = "*")
public class BgImageController {

	final Logger logger = LoggerFactory.getLogger(BgImageController.class);

	@Autowired
	private BgImageService bgImageServiceImpl;

	@GetMapping("/bgImageList")
	public List<Long> list(@RequestHeader(name = "userId", required = true) String userId) {

		return bgImageServiceImpl.getBgImageSeqList(userId);
	}

	@PostMapping("/regImage")
	public ServerResponse registration(MultipartHttpServletRequest request) throws Exception {

		List<MultipartFile> fileList = request.getFiles("file");

		return bgImageServiceImpl.registrationImage(fileList);
	}

	@GetMapping("/getImage/{fileSeq}")
	public ResponseEntity<byte[]> getImage(@PathVariable(name = "fileSeq", required = true) Long fileSeq) {
		return bgImageServiceImpl.getImage(fileSeq);
	}
	
	@DeleteMapping("/deleteImage/{fileSeq}")
	public ServerResponse deleteImage(@PathVariable(name = "fileSeq", required = true) Long fileSeq) {
		return bgImageServiceImpl.deleteImage(fileSeq);
	}
}
