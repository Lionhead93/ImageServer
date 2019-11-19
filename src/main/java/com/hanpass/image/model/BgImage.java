package com.hanpass.image.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.hanpass.image.type.Extension;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "BG_IMAGE")
public class BgImage {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BG_IMAGE_SEQ")
	private Long bgImageSeq;
	
	@Column(name = "USER_ID", length = 100, nullable = false)
	private String userId;
		
	@Column(name = "EXTENSION", length = 100, nullable = false)
	@Enumerated(EnumType.STRING)
	private Extension extension;
		
	@Column(name = "REG_DATE", nullable = false, updatable = false)
	@CreationTimestamp
	protected LocalDateTime regDate;	
	
	public String getFileName() {
		return String.format("%s.%s", bgImageSeq, extension.name().toLowerCase());
	}
}
