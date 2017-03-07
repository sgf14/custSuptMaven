package com.prod.custSuptMaven.site.repositories;

public class SearchResult<T> {
	private final T entity;
	private final double relevance;
	
	public SearchResult(T entity, double relevance) {
		this.entity = entity;
		this.relevance = relevance;
	}

	public T getEntity() {
		return entity;
	}

	public double getRelevance() {
		return relevance;
	}

}
