package com.sbs.untact.controller;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;


@Controller
public class AdmArticleController {

	@Autowired
	private ArticleService articleService;
	

	// 게시물 상세보기
	@RequestMapping("/adm/article/detail")
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
	@RequestMapping("/adm/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId, String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		Board board = articleService.getBoard(boardId);
		if(board == null) {
			return new ResultData("F-1", "존재하지 않는 게시판 입니다.");
		}
		
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
		int itemsInAPage = 20;	//한페이당 보여주는 게시물개수
		List<Article> articles = articleService.getForPrintArticles(boardId, searchKeywordType, searchKeyword, page, itemsInAPage);
		return new ResultData("S-1", "성공", "articles", articles);
	}
	
	
	// 게시물 추가
	@RequestMapping("/adm/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");

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
	@RequestMapping("/adm/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
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
			

	@RequestMapping("/adm/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpServletRequest req){
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		
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
	
	
	// 댓글 추가
	@RequestMapping("/adm/article/doAddReply")
	@ResponseBody
	public ResultData doAddReply(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");

		if(param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요. 제발");
		}
		if(param.get("articleId") == null) {
			return new ResultData("F-1", "articleId를 입력해주세요.");
		}
		
		param.put("memberId", loginedMemberId);
		return articleService.addReply(param);
	}
	

}
	
	
