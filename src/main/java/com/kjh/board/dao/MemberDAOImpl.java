package com.kjh.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.vo.KjhMemberVO;

import lombok.extern.log4j.Log4j;

@Log4j(topic = "MemberDAOImpl")
@Repository
public class MemberDAOImpl implements MemberDAO{
	
	//@Autowired
	//SqlSession sqlSession;	//아이바티스
	@Autowired
	SqlSessionTemplate sqlsessionT; //마이바티스 멀티쓰래드 환경에서 유리 되도록 이걸쓰자 
	
	//회원가입
	@Override
	public int mem_insert(KjhMemberVO kjhmemberVO) {
	System.out.println("memberdao가 실행되었습니다.");
	System.out.println("mnum :::"+kjhmemberVO.getM_num());
	System.out.println("mid :::"+kjhmemberVO.getM_id());
	System.out.println("mpw :::"+kjhmemberVO.getM_pw() );
	System.out.println("mname :::"+kjhmemberVO.getM_name());
	System.out.println("mphone :::"+kjhmemberVO.getM_phone());
	System.out.println("m_email :::"+kjhmemberVO.getM_email());
	System.out.println("m_regdate :::"+kjhmemberVO.getM_reg_date());
	System.out.println("m_update_date :::"+kjhmemberVO.getM_update_date());
	//매개변수(주소값) = mappers.xml에 있는 namespace와 DDL문이다.
	//int result=sqlSession.insert("mapper.member.mem_insert"); 실행 x
	//int result=sqlSession.insert("mapper.member.mem_insert", kjhmemberVO); 
	int result=sqlsessionT.insert("mapper.member.mem_insert", kjhmemberVO);
	System.out.println("sql insert가 실행되었습니다.");
	
		return result;
	}
	// kvo에 id 값을 활용해서 DB에 이미 id값이 있는지 여부를 판별하는 DAO
	@Override
	public boolean mem_val_id(KjhMemberVO kjhmemberVO) {
		System.out.println("val_id 실행");
		System.out.println("vo에 id :::"+kjhmemberVO.getM_id());
		String result =sqlsessionT.selectOne("mapper.member.mem_val_id", kjhmemberVO);
		System.out.println("val_id 실행 값:::"+result);
		if(result == null) {
			return true;// 통과한것
		}
		return false; // 통과하지 못한것
	}
	
	//로그인
	// id와 pw를 조회해서 m_num이 있는지 여부 판별, 세션에 값을 주기 위해서
	@Override
	public String mem_select_m_num(KjhMemberVO kjhmemberVO) {
		/*
		 * Map map=sqlsessionT.selectOne("mapper.member.mem_select_m_num", kjhmemberVO);
		 *System.out.println("DAO에서 select_m_num 실행 후:::"+sqlsessionT.selectOne("mapper.member.mem_select_m_num", kjhmemberVO));
		 *String result=(String)map.get("m_num");
		 *System.out.println(result);
		*/
		String result = sqlsessionT.selectOne("mapper.member.mem_select_m_num", kjhmemberVO);
		System.out.println("DAO에서 select_m_num 실행 후 :::"+result);
		
		return result;
	}
	
	//마이페이지에서 나의 회원값
	// 실험 return 타입을 일반 타입으로 하고 sql을 실행해도 vo에 담아서 올까?
	//만약 return 타입을 vo 객체로 한다면 vo에 담아서 올까?
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
//		System.out.println(kjhmemberVO.getM_num());
//		System.out.println(kjhmemberVO.getM_id());
//		System.out.println(kjhmemberVO.getM_pw());
//		System.out.println(kjhmemberVO.getM_name());	
//		System.out.println(kjhmemberVO.getM_phone());
//		System.out.println(kjhmemberVO.getM_email());
//		System.out.println(kjhmemberVO.getM_addr());
		int a=sqlsessionT.update("mapper.member.mem_update",kjhmemberVO);
	
		return a;
	}

	@Override
	public int mem_delete(KjhMemberVO kjhmemberVO) {
		int result=sqlsessionT.delete("mapper.member.mem_delete", kjhmemberVO);
		return result;
	}

	
}
