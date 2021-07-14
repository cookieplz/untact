package com.sbs.untact.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.ReplyService;

@Controller
public class UsrReplyController {

		@Autowired
		ReplyService replyService;
		
		@Autowired
		ArticleService articleService;
		
		
		// 게시물 리스트 출력
		@RequestMapping("/usr/reply/list")
		@ResponseBody
		public ResultData showList(String relTypeCode, Integer relId) {	// 관련된 타입코드, 관련된 아이디. articleId로 하지않고 relId로 함으로써 글뿐만 아니라 어디에도 댓글을 달 수 있음 예컨대 특정 상품, 앱에 달린 리뷰.  
			if(relId == null) {
				return new ResultData("F-1", "id를 입력해주세요.");
			}
			
			if(relTypeCode.equals("article")) {
				Article article = articleService.getArticle(relId);
				if(article == null) {
					return new ResultData("F-1", "존재하지 않는 게시물 입니다.");
				}
			}
			List<Reply> replies = replyService.getForPrintReplies(relTypeCode, relId);
			return new ResultData("S-1", "성공", "replies", replies);		
		}
		
}
