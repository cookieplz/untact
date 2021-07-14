package com.sbs.untact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ReplyDao;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;

@Service
public class ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private MemberService memberService;
	
	
	// 댓글 하나 가지고 오기
	public Reply getReply(Integer id) {
		return replyDao.getReply(id);
	}

	// 댓글 리스트 예쁘게 가지고오기
	public List<Reply> getForPrintReplies(String relTypeCode, int relId) {
		return replyDao.getForPrintReplies(relTypeCode, relId);
	}

	// 게시글 삭제시 권한 체크
	public ResultData getActorCanDeleteRd(Reply reply, int actorId) {
		if(reply.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}
		if(memberService.isAdmin(actorId)) {
			return new ResultData("S-2", "가능합니다.");
		}
		return new ResultData("F-1", "권한이 없습니다.");
	}

	// 게시글 삭제하기
	public ResultData deleteReply(Integer id) {
		replyDao.deleteReply(id);
		return new ResultData("S-1", "삭제하였습니다.", "id", id);
	}

	// 게시글 수정시 권한체크
	public ResultData getActorCanModifyRd(Reply reply, int actorId) {
		return getActorCanDeleteRd(reply, actorId);
	}

	// 게시글 수정하기
	public ResultData modifyReply(int id, String body) {
		replyDao.modifyReply(id, body);
		return new ResultData("S-1", "댓글을 수정하였습니다.", "id", id);
	}
	
}
