package com.sbs.untact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {
	private int id;
	private String regDate;
	private String updateDate;
	private int memberId;
	private String title;
	private String body;
	private String extra__writer;
	
}
