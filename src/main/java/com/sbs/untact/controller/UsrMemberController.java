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
import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.MemberService;


@Controller
public class UsrMemberController {
	
	@Autowired
	private MemberService memberService;

	// 회원가입
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if(param.get("loginId") == null) {
			return new ResultData("F-1", "아이디를 입력해주세요. 제발");
		}
		Member existingMember= memberService.getMemberByLoginId((String) param.get("loginId"));
		if(existingMember != null) {
			return new ResultData("F-2", String.format("%s (은)는 이미 사용중인 아이디 입니다.",  param.get("loginId")));   
		}
		
		if(param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요. 제발");
		}
		if(param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해주세요. 제발");
		}
		if(param.get("nickname") == null) {
			return new ResultData("F-1", "닉네임을 입력해주세요. 제발");
		}
		if(param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "전화번호를 입력해주세요. 제발");
		}
		if(param.get("email") == null) {
			return new ResultData("F-1", "이메일을 입력해주세요. 제발");
		}
		return memberService.join(param);
	}

	
	// 로그인
	@RequestMapping("/usr/member/doLogin")
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
		// 세션~
		session.setAttribute("loginedMemberId", existingMember.getId());
		return new ResultData("S-1", String.format("%s님 환영합니다♥", existingMember.getNickname()));
	}
	
	
	// 로그아웃
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public ResultData doLogout(HttpSession session) {

		session.removeAttribute("loginedMemberId");
		return new ResultData("S-1", "로그아웃 되었습니다.");
	}
	
	
	// 회원수정
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpSession session) {
		if(param.isEmpty()) {
			return new ResultData("F-2", "수정할 정보를 입력해주세요.");
		}
		int loginedMemberId = (int)session.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMember(param);
	}
	
}
	
	
