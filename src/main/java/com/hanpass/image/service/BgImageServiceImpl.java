package com.hanpass.image.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hanpass.image.dto.DeleteImageResponse;
import com.hanpass.image.dto.RegImageResponse;
import com.hanpass.image.model.BgImage;
import com.hanpass.image.repository.BgImageRepository;
import com.hanpass.image.type.Extension;

@Service
public class BgImageServiceImpl implements BgImageService {

	final Logger logger = LoggerFactory.getLogger(BgImageService.class);

	@Autowired
	private BgImageRepository bgImageRepository;

	@Value(value = "${hpImgServer.ImageRoot}")
	private String filePath;

	@Override
	public List<Long> getBgImageSeqList(String userId) {
		List<BgImage> list = bgImageRepository.findByUserId(userId);
		List<Long> result = new ArrayList<Long>();

		for (BgImage n : list) {
			result.add(n.getBgImageSeq());
		}

		return result;
	}
	
	@Override
	public RegImageResponse registrationImage(List<MultipartFile> fileList) throws Exception {

		String userId = "testUser";

		File fileDir = new File(filePath);

		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		for (MultipartFile mf : fileList) {

			String originFileName = mf.getOriginalFilename();
			String contentType = originFileName.substring(originFileName.lastIndexOf(".")+1).toUpperCase();
			
			BgImage savedImage = addFile(BgImage.builder().userId(userId).extension(Extension.valueOf(contentType)).build());
				
			String saveFileName = 
					String.format("%s.%s", savedImage.getBgImageSeq(), savedImage.getExtension().name().toLowerCase());
						
			logger.info("### originFileName : {} ###", originFileName);
			logger.info("### saveFileName : {} ###", saveFileName);
			logger.info("### filePath : {} ###", filePath);
			try {

				mf.transferTo(new File(filePath, saveFileName));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}

		return new RegImageResponse("ok", "0000");
	}	

	@Override
	public ResponseEntity<byte[]> getImage(Long fileSeq) {
		
		Optional<BgImage> op = bgImageRepository.findById(fileSeq);
		
		String fileName = "";
		
		if (op.isPresent()) {
			fileName = op.get().getFileName();
			logger.info("### getFileName : {} ###", fileName);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, fileSeq.toString());
		}

		ResponseEntity<byte[]> entity = null;

		File file = new File(filePath + "/" + fileName);
		if (!file.exists())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, fileName);

		InputStream is = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(getMediaTypeFromFileName(fileName));

			is = new FileInputStream(file);

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.CREATED);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, fileName, ex);
		} finally {
			try {
				is.close();
			} catch (Exception ignore) {
			}
		}

		return entity;
	}

	@Override
	public DeleteImageResponse deleteImage(Long fileSeq) {
		logger.info("### deleteFileSeq : {} ###", fileSeq);

		Optional<BgImage> op = bgImageRepository.findById(fileSeq);

		BgImage bgImage = null;

		if (op.isPresent()) {
			bgImage = op.get();
		} else {
			return new DeleteImageResponse("Not Found : " + fileSeq.toString(), "9999");
		}

		String fileName = bgImage.getFileName();

		File file = new File(filePath + "/" + fileName);

		if (file.exists()) {
			if (!file.delete()) {
				return new DeleteImageResponse("DELETE FAIL : " + fileName, "9999");
			}
		} else {
			return new DeleteImageResponse("Not Found : " + fileName, "9999");
		}

		bgImageRepository.delete(bgImage);

		return new DeleteImageResponse("ok", "0000");
	}

	public MediaType getMediaTypeFromFileName(String fileName) {
		if (fileName == null)
			return null;

		fileName = fileName.trim();
		if (fileName.equals(""))
			return null;

		String extName = fileName.toUpperCase().substring(fileName.lastIndexOf(".") + 1);
		if (extName.equals(""))
			return null;

		if (extName.equals("JPG") || extName.equals("JPEG"))
			return MediaType.IMAGE_JPEG;
		if (extName.equals("GIF"))
			return MediaType.IMAGE_GIF;
		if (extName.equals("PNG"))
			return MediaType.IMAGE_PNG;

		if (extName.equals("PDF"))
			return MediaType.APPLICATION_PDF;

		return null;
	}
	
	public BgImage addFile(BgImage bgImage) {

		return bgImageRepository.save(bgImage);
	}

}
