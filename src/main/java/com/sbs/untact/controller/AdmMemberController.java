package com.sbs.untact.controller;

import java.util.List;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.MemberService;


	@Controller
	public class AdmMemberController {
	
	@Autowired
	private MemberService memberService;

	@RequestMapping("/adm/member/login")
	public String login() {
		return "adm/member/login";
	}
	

	// 로그인
	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, /*세션~*/HttpSession session) {
	//------------------------------------절취선----------------------------------
		if(loginId == null) {
			return new ResultData("F-1", "아이디를 입력해주세요. 제발");
		}
		Member existingMember= memberService.getMemberByLoginId(loginId);
		if(existingMember == null) {
			return new ResultData("F-2", String.format("%s (은)는 존재하지 않는 아이디 입니다.", loginId));   
		}
		
		if(loginPw == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요. 제발");
		}
	
		if(existingMember.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}
		if(memberService.isAdmin(existingMember) == false) {
			return new ResultData("F-4", "관리자만 접속할 수 있는 페이지 입니다.");
		}
		
		// 세션~
		session.setAttribute("loginedMemberId", existingMember.getId());
		return new ResultData("S-1", String.format("%s님 환영합니다♥", existingMember.getNickname()));
	}
	
	
	
	// 회원수정
	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		if(param.isEmpty()) {
			return new ResultData("F-2", "수정할 정보를 입력해주세요.");
		}
		int loginedMemberId = (int)req.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMember(param);
	}
	
}
	
	
