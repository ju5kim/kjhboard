package com.kjh.board.vo;

public class KjhBoardVO {
	
	private String b_num;
	private String b_subject;
	private String b_content;
	private String b_counts;
	private String b_images;
	private String b_reg_date;
	private String b_update_date;
	private String m_num;

	//여기서 페이지 처리를 하기위한 프로퍼티를 만들어야 하는데

	public KjhBoardVO() {
		
	}

	public KjhBoardVO(String b_num, String b_subject, String b_content, String b_counts, String b_images,
			String b_reg_date, String b_update_date, String m_num) {
		this.b_num = b_num;
		this.b_subject = b_subject;
		this.b_content = b_content;
		this.b_counts = b_counts;
		this.b_images = b_images;
		this.b_reg_date = b_reg_date;
		this.b_update_date = b_update_date;
		this.m_num = m_num;
	}

	public String getB_num() {
		return b_num;
	}

	public void setB_num(String b_num) {
		this.b_num = b_num;
	}

	public String getB_subject() {
		return b_subject;
	}

	public void setB_subject(String b_subject) {
		this.b_subject = b_subject;
	}

	public String getB_content() {
		return b_content;
	}

	public void setB_content(String b_content) {
		this.b_content = b_content;
	}

	public String getB_counts() {
		return b_counts;
	}

	public void setB_counts(String b_counts) {
		this.b_counts = b_counts;
	}

	public String getB_images() {
		return b_images;
	}

	public void setB_images(String b_images) {
		this.b_images = b_images;
	}

	public String getB_reg_date() {
		return b_reg_date;
	}

	public void setB_reg_date(String b_reg_date) {
		this.b_reg_date = b_reg_date;
	}

	public String getB_update_date() {
		return b_update_date;
	}

	public void setB_update_date(String b_update_date) {
		this.b_update_date = b_update_date;
	}

	public String getM_num() {
		return m_num;
	}

	public void setM_num(String m_num) {
		this.m_num = m_num;
	}

		
	
}
