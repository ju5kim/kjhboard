package com.kjh.board.vo;

public class PageVO {
	private String now_page;
	private String now_group;

	private String start_row;
	private String end_row;
	private String real_end_row;

	private String start_page;
	private String end_page;
	private String real_end_page;

	private String page_per_rows;
	private String group_per_pages;

	public PageVO() {
	}

	public PageVO(String now_page, String now_group, String start_row, String end_row, String real_end_row,
			String start_page, String end_page, String real_end_page, String page_per_rows, String group_per_pages) {
		this.now_page = now_page;
		this.now_group = now_group;
		this.start_row = start_row;
		this.end_row = end_row;
		this.real_end_row = real_end_row;
		this.start_page = start_page;
		this.end_page = end_page;
		this.real_end_page = real_end_page;
		this.page_per_rows = page_per_rows;
		this.group_per_pages = group_per_pages;
	}

	public String getNow_page() {
		return now_page;
	}

	public void setNow_page(String now_page) {
		this.now_page = now_page;
	}

	public String getNow_group() {
		return now_group;
	}

	public void setNow_group(String now_group) {
		this.now_group = now_group;
	}

	public String getStart_row() {
		return start_row;
	}

	public void setStart_row(String start_row) {
		this.start_row = start_row;
	}

	public String getEnd_row() {
		return end_row;
	}

	public void setEnd_row(String end_row) {
		this.end_row = end_row;
	}

	public String getReal_end_row() {
		return real_end_row;
	}

	public void setReal_end_row(String real_end_row) {
		this.real_end_row = real_end_row;
	}

	public String getStart_page() {
		return start_page;
	}

	public void setStart_page(String start_page) {
		this.start_page = start_page;
	}

	public String getEnd_page() {
		return end_page;
	}

	public void setEnd_page(String end_page) {
		this.end_page = end_page;
	}

	public String getReal_end_page() {
		return real_end_page;
	}

	public void setReal_end_page(String real_end_page) {
		this.real_end_page = real_end_page;
	}

	public String getPage_per_rows() {
		return page_per_rows;
	}

	public void setPage_per_rows(String page_per_rows) {
		this.page_per_rows = page_per_rows;
	}

	public String getGroup_per_pages() {
		return group_per_pages;
	}

	public void setGroup_per_pages(String group_per_pages) {
		this.group_per_pages = group_per_pages;
	}

}
