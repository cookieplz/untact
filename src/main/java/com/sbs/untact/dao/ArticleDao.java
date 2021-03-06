package com.sbs.untact.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;


// Dao -> 단순 데이터 처리를 위한 창고
// select가 아니면 리턴할 수 없음
@Mapper // 마이바티스를 여기다 적용한다... 지가 자동으로 해줌 개꿀 
public interface ArticleDao {

	public Article getArticle(@Param(value="id") int id);

	public void addArticle(Map<String, Object> param);

	public void deleteArticle(@Param(value="id")int id);

	public void modifyArticle(@Param(value="id")int id, @Param(value="title")String title, @Param(value="body")String body);
	
	public List<Article> getArticles(@Param(value="searchKeywordType")String searchKeywordType, @Param(value="searchKeyword")String searchKeyword);

	public Article getForPrintArticle(@Param("id")int id);

	public List<Article> getForPrintArticles(@Param("boardId") int boardId,
			@Param("searchKeywordType") String searchKeywordType, @Param("searchKeyword") String searchKeyword,
			@Param("limitStart") int limitStart, @Param("limitTake") int limitTake);
	
	public Board getBoard(@Param("id") int id);

	public void addReply(Map<String, Object> param);

}
