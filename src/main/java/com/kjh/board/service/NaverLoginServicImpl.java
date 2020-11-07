package com.kjh.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kjh.board.dao.NaverLoginDAOImpl;
import com.kjh.board.vo.KjhMemberVO;

@Transactional
@Service
public class NaverLoginServicImpl implements NaverLoinService{
	
	@Autowired
	NaverLoginDAOImpl naverLoginDAO;
	
	@Override
	public int naver_insert(KjhMemberVO kvo) {
		int result;
		//result =naverLoginDAO.naverInsert(memberVO);
		System.out.println("서비스 실행");
		result=naverLoginDAO.naver_insert(kvo);
		System.out.println("서비스 실행완료");
		return result;
	}

	@Override
	public KjhMemberVO naver_login(KjhMemberVO kvo) {
		kvo=naverLoginDAO.naver_login(kvo);
		return kvo;
	}

	@Override
	public String naver_id_db_chek(KjhMemberVO kvo) {
		String m_num=naverLoginDAO.naver_id_db_chek(kvo);
		return m_num;
	}

	
}
