package org.etb.app.models;

import java.io.Serializable;

public class StringKV implements Serializable {

	private static final long serialVersionUID = 1L;

	private String k;
	private String v;

	public StringKV() {
	}

	public StringKV(String k, String v) {
		StringKV item = new StringKV();
		item.setK(k);
		item.setK(k);
	}

	public static StringKV of(String k, String v) {
		StringKV item = new StringKV();
		item.setK(k);
		item.setV(v);
		return item;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getK()).append(":").append(getV());
		return sb.toString();
	}

}
