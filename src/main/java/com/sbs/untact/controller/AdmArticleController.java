package com.sbs.untact.controller;

import java.util.List;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.GenFile;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.GenFileService;
import com.sbs.untact.util.Util;


@Controller
public class AdmArticleController extends BaseController{

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private GenFileService genFileService;

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
	public String showList(HttpServletRequest req, @RequestParam(defaultValue = "1") int boardId, String searchKeywordType, String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		Board board = articleService.getBoard(boardId);
		req.setAttribute("board", board);
		if(board == null) {
			return msgAndBack(req, "존재하지 않는 게시판 입니다.");
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
		for ( Article article : articles ) {
			GenFile genFile = genFileService.getGenFile("article", article.getId(), "common", "attachment", 1);
			if ( genFile != null ) {
				article.setExtra__thumbImg(genFile.getForPrintUrl());
			}
		}
		req.setAttribute("articles", articles);
		
		return "adm/article/list";
	}
	
	
	// 게시물 추가(글쓰기) 폼 들어가기
	@RequestMapping("/adm/article/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/article/add";
	}
	
	
	// 게시물 추가
	@RequestMapping("/adm/article/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req,
			MultipartRequest multipartRequest) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (param.get("title") == null) {
			return msgAndBack(req, "title을 입력해주세요.");
		}

		if (param.get("body") == null) {
			return msgAndBack(req, "body를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		ResultData addArticleRd = articleService.addArticle(param);

		int newArticleId = (int) addArticleRd.getBody().get("id");

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
			
			if ( multipartFile.isEmpty() == false ) {
				genFileService.save(multipartFile, newArticleId);				
			}
		}
		return msgAndReplace(req, String.format("%d번 게시물이 작성되었습니다.", newArticleId), "../article/detail?id=" + newArticleId);
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
	
	
