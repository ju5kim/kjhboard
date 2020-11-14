package com.kjh.board.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kjh.board.controller.BoardController;
import com.kjh.board.dao.MemberDAO;
import com.kjh.board.util.SHA256Util;
import com.kjh.board.vo.KjhMemberVO;

@Transactional
@Service
public class MemberServiceImpl implements MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
	@Autowired
	MemberDAO memberdao;

	// 회원가입
	@Override
	public boolean mem_insert(KjhMemberVO kjhmembervo, HttpServletRequest request) {
		KjhMemberVO kvo = resetVO(kjhmembervo, request);
		// 지금 여기서 회원가입 실행 전에 회원 id가 있는지 여부를 판단하고 실행시키는게 좋다.
		if (memberdao.mem_val_id(kvo)) {
			int result = memberdao.mem_insert(kvo);

			if (result > 0) {
				return true;
			} else {
				return false;
			} // end of mem_insert
		} else {
			return false;
		} // end of mem_val_id
	}

	// VO에 값을 새롭게 셋팅해서 kvo에 넣는 작업
	public KjhMemberVO resetVO(KjhMemberVO kjhMemberVO, HttpServletRequest request) {
		KjhMemberVO kvo = new KjhMemberVO();
		kvo = kjhMemberVO;
		kvo.setM_phone(request.getParameter("m_phone0") + "-" + request.getParameter("m_phone1") + "-"
				+ request.getParameter("m_phone2"));
		kvo.setM_email(request.getParameter("m_email0") + "@" + request.getParameter("m_email1"));
		kvo.setM_addr(request.getParameter("m_addr0") + "/" + request.getParameter("m_addr1") + "/"
				+ request.getParameter("m_addr2"));
		return kvo;
	}

	// 로그인
	@Override
	public boolean mem_longin(KjhMemberVO kvo, HttpServletRequest request) {
		// 만약 로그인에 성공했으면 세션값을 부여하고 메인페이지로 보내자.
		HttpSession session = request.getSession();
		log.info("로그인 처음  pw 값 ::: "+kvo.getM_pw());
		// DB에 salt값을 가지고와서
		// salt값과 사용자입력값을 다시 암호화한다.
		// 그러면 pw 값이 나온다 그걸 조회해서 맞으면 돌려준다.
		String salt= memberdao.select_salt(kvo); // id에 대한 salt값 구하기
		kvo.setSalt(salt);
		log.info("로그인 시 salt 값 ::: "+salt);
		String pw = kvo.getM_pw();
		log.info("로그인 시 pw 값 ::: "+pw);
		SHA256Util sha256Util = new SHA256Util();
		
		String pw_sha = sha256Util.getEncrypt(pw, salt);
		kvo.setM_pw(pw_sha);
		log.info("로그인 시 pw 값 암호화된 ::: "+pw_sha);
		
		kvo = memberdao.mem_select_m_num(kvo); // id와 pw로 회원번호를 조회하기
		if (kvo.getM_num() != null) {
			session.setAttribute("m_num", kvo.getM_num());
			return true;
		};
		return false;
	}

	// 마이페이지에서 나의 회원값
	@Override
	public KjhMemberVO mem_select_kvo(KjhMemberVO kvo) {
		kvo = memberdao.mem_select_kvo(kvo);
		return kvo;
	}

	@Override
	public String mem_select_id(KjhMemberVO kvo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mem_select_all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int mem_update(KjhMemberVO kjhmemberVO, HttpServletRequest request) {
		kjhmemberVO = resetVO(kjhmemberVO, request);
		int result = memberdao.mem_update(kjhmemberVO);
		log.info("mem_update 서비스 실행완료");
		return result;
	}

	@Override
	public boolean mem_delete(KjhMemberVO kjhmemberVO) {
		int result = memberdao.mem_delete(kjhmemberVO);
		return false;
	}

}
