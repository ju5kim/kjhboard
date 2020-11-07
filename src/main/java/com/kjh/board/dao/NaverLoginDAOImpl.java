package com.kjh.board.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.vo.KjhMemberVO;

@Repository
public class NaverLoginDAOImpl implements NaverLoginDAO {

	@Autowired
	SqlSessionTemplate sqlsessionTemplate;

	@Override
	public int naver_insert(KjhMemberVO kvo) {
		int result = sqlsessionTemplate.insert("mapper.member.naver_insert",kvo);
		return result;
	}
	
	@Override
	public KjhMemberVO naver_login(KjhMemberVO kvo) {
		kvo = sqlsessionTemplate.selectOne("mapper.member.naver_login", kvo);
		return kvo;

	}
	@Override
	public String naver_id_db_chek(KjhMemberVO kvo) {
		String m_id=sqlsessionTemplate.selectOne("mapper.member.naver_id_db_chek", kvo);
		return m_id;
	}
}
