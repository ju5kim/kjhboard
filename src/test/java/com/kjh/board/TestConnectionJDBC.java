package com.kjh.board;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.impl.Log4JLogger;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
public class TestConnectionJDBC {
	@Autowired
	Log4JLogger log;
	
	static {
		try {
			Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testConnetion() {
		try {
			Connection con = DriverManager.getConnection("jdbc:log4jdbc:oracle:thin:@localhost:1521:orcl","scott","tiger");
			log.info(con);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("므야");
		}
	}
}
