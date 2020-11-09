package com.kjh.board.dao;

import org.apache.commons.logging.impl.Log4JLogger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.vo.KjhMemberVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class MemberDAOImpl implements MemberDAO{

	//@Autowired
	//SqlSession sqlSession;	//아이바티스
	@Autowired
	SqlSessionTemplate sqlsessionT; //마이바티스 멀티쓰래드 환경에서 유리 되도록 이걸쓰자 
	
	//회원가입
	@Override
	public int mem_insert(KjhMemberVO kjhmemberVO) {
	int result=sqlsessionT.insert("mapper.member.mem_insert", kjhmemberVO);
		return result;
	}
	// kvo에 id 값을 활용해서 DB에 이미 id값이 있는지 여부를 판별하는 DAO
	@Override
	public boolean mem_val_id(KjhMemberVO kjhmemberVO) {
		String result =sqlsessionT.selectOne("mapper.member.mem_val_id", kjhmemberVO);
		if(result == null) {
			return true;// 통과한것
		}
		return false; // 통과하지 못한것
	}
	
	//로그인
	// id와 pw를 조회해서 m_num이 있는지 여부 판별, 세션에 값을 주기 위해서
	@Override
	public String mem_select_m_num(KjhMemberVO kjhmemberVO) {
		String result = sqlsessionT.selectOne("mapper.member.mem_select_m_num", kjhmemberVO);
		return result;
	}
	
	//마이페이지에서 나의 회원값
	// 실험 return 타입을 일반 타입으로 하고 sql을 실행해도 vo에 담아서 올까?
	//만약 return 타입을 vo 객체로 한다면 vo에 담아서 온다
	@Override
	public KjhMemberVO mem_select_kvo(KjhMemberVO kvo) {
		kvo=sqlsessionT.selectOne("mapper.member.mem_select_kvo", kvo);
		return kvo;
	}
	@Override
	public String mem_select_all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public int mem_update(KjhMemberVO kjhmemberVO) {
		int result=sqlsessionT.update("mapper.member.mem_update",kjhmemberVO);
	
		return result;
	}

	@Override
	public int mem_delete(KjhMemberVO kjhmemberVO) {
		int result=sqlsessionT.delete("mapper.member.mem_delete", kjhmemberVO);
		return result;
	}
	@Override
	public String select_salt(KjhMemberVO kvo) {
		String result=sqlsessionT.selectOne("mapper.member.select_salt",kvo);
		return result;
	}

	
}
