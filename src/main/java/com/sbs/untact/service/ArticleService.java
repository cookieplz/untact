package com.sbs.untact.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class ArticleService {
	private int articlesLastId;
	private List<Article> articles;
	
	public ArticleService() {
		// 멤버변수 초기화
		articlesLastId = 0;
		articles = new ArrayList<>();		
		
		// 게시물 2개 생성
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목2", "내용2"));
	}

	// 게시물 찾아오기
	public Article getArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}

	// 게시물 리스트 보여주기
	public List<Article> getArticles() {
		return articles;
	}

	
	// 게시물 추가하기
	public ResultData add(String title, String body) {
		int id = ++articlesLastId;
		String regDate = Util.getNowDateStr();
		String updateDate = regDate;
		articles.add(new Article(id, regDate, updateDate, title, body));
		return new ResultData("S-1", "성공하였습니다.", "id", articlesLastId);
	}

	
	// 게시물 삭제하기
	public ResultData deleteArticle(int id) {
		for(Article article : articles) {
			if(article.getId() == id) {
				articles.remove(article);
				return new ResultData("S-1", "삭제에 성공하였습니다.", "id", id);
			}
		}
		return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id" , id);
	}

	
	// 게시물 수정하기
	public ResultData modify(int id, String title, String body) {
		Article article = getArticle(id);
		article.setTitle(title); 
		article.setBody(body);
		article.setUpdateDate(Util.getNowDateStr());
		return new ResultData("S-1", "게시물을 수정하였습니다.", "id", id);
		
		
	}
	
}
