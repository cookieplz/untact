package com.sbs.untact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.ReplyService;
import com.sbs.untact.util.Util;

@Controller
public class AdmReplyController {

		@Autowired
		ReplyService replyService;
		
		@Autowired
		ArticleService articleService;
		
		
		// 댓글 리스트 출력
		@RequestMapping("/adm/reply/list")
		@ResponseBody
		public ResultData showList(String relTypeCode, Integer relId) {	// 관련된 타입코드, 관련된 아이디. articleId로 하지않고 relId로 함으로써 글뿐만 아니라 어디에도 댓글을 달 수 있음 예컨대 특정 상품, 앱에 달린 리뷰.  
			if(relTypeCode == null) {
				return new ResultData("F-1", "relTypeCode를 입력해주세요.");
			}
			
			if(relId == null) {
				return new ResultData("F-1", "relId를 입력해주세요.");
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
		
		
		// 댓글 삭제하기
		@RequestMapping("/adm/reply/doDelete")
		@ResponseBody
		public ResultData doDelete(Integer id, HttpServletRequest req) {	// 비포엑션에서 세션에 관해 다 때려넣었기 때문에 여기서는 걍 받기만 하면 됨
			int loginedMemberId = (int)req.getAttribute("loginedMemberId");
			
			if(id == null) {
				return new ResultData("F-1", "id를 입력해주세요.");
			}
			Reply reply = replyService.getReply(id);
			if(reply == null) {
				return new ResultData("F-1", "해당 댓글은 존재하지 않습니다.");
			}
			
			ResultData actorCanDeleteRd = replyService.getActorCanDeleteRd(reply, loginedMemberId);
			if(actorCanDeleteRd.isFail()) {
				return actorCanDeleteRd;
			}
			
			return replyService.deleteReply(id);
		}
		
		
		// 댓글 수정하기
		@RequestMapping("/adm/reply/doModify")
		@ResponseBody
		public ResultData doModify(Integer id, String body, HttpServletRequest req) {	// 비포엑션에서 세션에 관해 다 때려넣었기 때문에 여기서는 걍 받기만 하면 됨
			int loginedMemberId = (int)req.getAttribute("loginedMemberId");
			
			if(id == null) {
				return new ResultData("F-1", "id를 입력해주세요.");
			}
			if(body == null) {
				return new ResultData("F-1", "body를 입력해주세요.");
			}
			
			Reply reply = replyService.getReply(id);
			if(reply == null) {
				return new ResultData("F-1", "해당 댓글은 존재하지 않습니다.");
			}
			
			ResultData actorCanModifyRd = replyService.getActorCanModifyRd(reply, loginedMemberId);
			if (actorCanModifyRd.isFail()) {
				return actorCanModifyRd;
			}		
			return replyService.modifyReply(id, body);
		}
		
}
