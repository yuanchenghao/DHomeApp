package com.dejia.anju.model;

import org.kymjs.aframe.database.annotate.Id;

public class HistorySearchWords {

	@Id()
	private int id;
	private String hwords;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the hwords
	 */
	public String getHwords() {
		return hwords;
	}

	/**
	 * @param hwords
	 *            the hwords to set
	 */
	public void setHwords(String hwords) {
		this.hwords = hwords;
	}

}
