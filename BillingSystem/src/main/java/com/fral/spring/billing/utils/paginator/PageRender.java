package com.fral.spring.billing.utils.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	
	private int totalPages;
	private int itemsPerPage;
	private int currentPage;
	private List<PageItem> pages;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<>();
		
		int from, to;
		if (totalPages <= itemsPerPage) {
			from = 1;
			to = totalPages;
		} else {
			if (totalPages <= itemsPerPage/2) {
				from = 1;
				to = itemsPerPage;
			} else if (currentPage >= totalPages - itemsPerPage/2) {
				from = totalPages - itemsPerPage + 1;
				to = itemsPerPage;
			} else {
				from = currentPage - itemsPerPage/2;
				to = itemsPerPage;
			}
		}
		
		for (int i = 0; i < to; i++) {
			pages.add(new PageItem(from + i, currentPage == from+1));
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public List<PageItem> getPages() {
		return pages;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean hasNext() {
		return page.hasNext();
	}
	
	public boolean hasPrevious() {
		return page.hasPrevious();
	}
 }
