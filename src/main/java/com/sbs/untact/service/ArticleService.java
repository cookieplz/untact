package com.sbs.untact.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ArticleDao;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;


@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MemberService memberService;

	// 게시물 찾아오기
	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	
	// 게시물 리스트 보여주기(제목 검색기능 추가)
	public List<Article> getArticles(String searchKeywordType, String searchKeyword) {
		return articleDao.getArticles(searchKeywordType, searchKeyword);
	}


	// 게시물 추가하기
	public ResultData addArticle(Map<String, Object> param) {
		articleDao.addArticle(param);
		int id = Util.getAsInt(param.get("id"), 0);
		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	
	// 게시물 삭제하기
	public ResultData deleteArticle(int id) {
		articleDao.deleteArticle(id);
		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	
	// 게시물 수정하기
	public ResultData modifyArticle(int id, String title, String body) {
		articleDao.modifyArticle(id, title, body);
		return new ResultData("S-1", "게시물을 수정하였습니다.", "id", id);
		
		
	}

	//게시물 수정시 권한 체크
	public ResultData getActorCanModifiyRd(Article article, int actorId) {
		if(article.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}
		if(memberService.isAdmin(actorId)) {
			return new ResultData("S-2", "가능합니다.");
		}
		return new ResultData("F-1", "권한이 없습니다.");
	}


	//게시글 삭제시 권한 체크
	public ResultData getActorCanDeleteRd(Article article, int actorId) {
		return getActorCanModifiyRd(article, actorId);	// 어차피 삭제권한이나 수정권한이나 비슷해서 재활용 개꿀
	}

	
	// 게시글 상세보기에서 닉네임 등 자세히 표시
	public Article getForPrintArticle(int id) {
		return articleDao.getForPrintArticle(id);
	}
	
}
