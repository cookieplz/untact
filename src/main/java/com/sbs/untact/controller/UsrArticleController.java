package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;


@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;
	

	// 게시물 상세보기
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		Article article = articleService.getArticle(id);
		return article;
	}
	
	
	// 게시물 리스트 출력
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public List<Article> showList(/*@RequestParam(defaultValue = "titleAndBody")*/ String searchKeywordType, String searchKeyword) {
		if(searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}
		if(searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}
		//------------------------------------절취선-------------------------------------
		if(searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}
		if(searchKeyword != null) {
			searchKeyword =searchKeyword.trim();
		}
		//------------------------------------절취선-------------------------------------
		if(searchKeyword == null) {
			searchKeywordType = null;
		}
		return articleService.getArticles(searchKeywordType, searchKeyword);
	}
	
	
	// 게시물 추가
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param) {
		if(param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요. 제발");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요. 제발");
		}
		ResultData rsData = articleService.addArticle(param);
		return rsData;	//과장님이 보고서 대신 써줌. 과장님 감사링~
	}
	

	// 게시물 삭제하기
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요. 제발");
		}
			Article article = articleService.getArticle(id);
			if(article == null) {
				return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
			}
			return articleService.deleteArticle(id);
		}
			

	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam(defaultValue = "0") Integer id, String title, String body){
		if(id == null) {
			return new ResultData("F-1" , "id를 입력해주세요.");
		}
		if(title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if(body == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		
		Article article = articleService.getArticle(id);
		if(article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다. 헐;;", "id", id);
		}
		return articleService.modifyArticle(id, title, body);
	}
	
}
	
	
