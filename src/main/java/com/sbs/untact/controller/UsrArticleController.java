package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.util.Util;


@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;
	

	// 게시물 상세보기
	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요");
		}
		Article article = articleService.getForPrintArticle(id);	//ForPrint -> 출력용으로 풍성한 데이터를 가져옴
		if(article == null) {
			return new ResultData("F-2", "존재하지 않는 게시물번호 입니다.");
		}
		return new ResultData("S-1", "성공", "article", article);
	}
	
	
	// 게시물 리스트 출력
	@RequestMapping("/usr/article/list")
	@ResponseBody
	public ResultData showList(/*@RequestParam(defaultValue = "titleAndBody")*/ String searchKeywordType, String searchKeyword) {
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
		List<Article> articles = articleService.getForPrintArticles(searchKeywordType, searchKeyword);
		return new ResultData("S-1", "성공", "articles", articles);
	}
	
	
	// 게시물 추가
	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if(param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요. 제발");
		}
		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요. 제발");
		}
		param.put("memberId", loginedMemberId);
		return articleService.addArticle(param);
	}
	

	// 게시물 삭제하기
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
		if(id == null) {
			return new ResultData("F-1", "id를 입력해주세요. 제발");
		}
			Article article = articleService.getArticle(id);
			if(article == null) {
				return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.", "id", id);
			}
			ResultData actorCanDeleteRd= articleService.getActorCanDeleteRd(article, loginedMemberId);
			if(actorCanDeleteRd.isFail()) {
				return actorCanDeleteRd;
			}
			return articleService.deleteArticle(id);
		}
			

	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpSession session){
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		
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
		ResultData actorCanModifyRd= articleService.getActorCanModifiyRd(article, loginedMemberId);
		if(actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		return articleService.modifyArticle(id, title, body);
	}
	
}
	
	
