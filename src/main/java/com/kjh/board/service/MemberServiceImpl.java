package com.kjh.board.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kjh.board.dao.MemberDAO;
import com.kjh.board.vo.KjhMemberVO;

@Transactional
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO memberdao;

	//회원가입 
	@Override
	public boolean mem_insert(KjhMemberVO kjhmembervo, HttpServletRequest request) {
		KjhMemberVO kvo = resetVO(kjhmembervo, request);
		//지금 여기서 회원가입 실행 전에 회원 id가 있는지 여부를 판단하고 실행시키는게 좋다.
		if(memberdao.mem_val_id(kvo)) {
			int result = memberdao.mem_insert(kvo);
			
			if (result > 0) {
				return true;
			} else {
				return false;
			}//end of mem_insert	
		}else {
			return false;
		}//end of mem_val_id
	}
	
	// VO에 값을 새롭게 셋팅해서 kvo에 넣는 작업
	public KjhMemberVO resetVO(KjhMemberVO kjhMemberVO, HttpServletRequest request) {
		KjhMemberVO kvo = new KjhMemberVO();
		kvo = kjhMemberVO;
		kvo.setM_phone(
				request.getParameter("m_phone0") +"-"+ request.getParameter("m_phone1") +"-"+ request.getParameter("m_phone2"));
		kvo.setM_email(request.getParameter("m_email0") + "@" + request.getParameter("m_email1"));
		kvo.setM_addr(request.getParameter("m_addr0") + "/" + request.getParameter("m_addr1") + "/"
				+ request.getParameter("m_addr2"));
		return kvo;
	}
	
	//로그인
	@Override
	public boolean mem_longin(KjhMemberVO kvo,HttpServletRequest request) {
		// 만약 회원가입에 성공했으면 세션값을 부여하고 메인페이지로 보내자.
		HttpSession session=request.getSession();
		String m_num = memberdao.mem_select_m_num(kvo); //id와 pw로 회원번호를 조회하기
		if(m_num!=null) {
			session.setAttribute("m_num", m_num);
			return true;
		};
		return false;
	}
	
	//마이페이지에서 나의 회원값
	@Override
	public KjhMemberVO mem_select_kvo(KjhMemberVO kvo) {
		kvo=memberdao.mem_select_kvo(kvo);
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
	public int mem_update(KjhMemberVO kjhmemberVO,HttpServletRequest request) {
		kjhmemberVO	= resetVO(kjhmemberVO, request);
		
//		System.out.println(kjhmemberVO.getM_num());
//		System.out.println(kjhmemberVO.getM_id());
//		System.out.println(kjhmemberVO.getM_pw());
//		System.out.println(kjhmemberVO.getM_name());	
//		System.out.println(kjhmemberVO.getM_phone());
//		System.out.println(kjhmemberVO.getM_email());
//		System.out.println(kjhmemberVO.getM_addr());
			int result=memberdao.mem_update(kjhmemberVO);
			System.out.println("mem_update 서비스 실행완료");
		return result;
	}

	@Override
	public boolean mem_delete(KjhMemberVO kjhmemberVO) {
	int result=memberdao.mem_delete( kjhmemberVO);
		return false;
	}

}
