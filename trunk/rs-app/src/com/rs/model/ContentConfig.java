package com.rs.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "content_config")
public class ContentConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_content_config")
	private Long idContentConfig;
	
	@Column(name="category")
	private String category;
	
	@Column(name="value1")
	private String value1;
	
	@Column(name="value2")
	private String value2;
	
	@Column(name="status", length=1, nullable = false)
	private String status;

	public Long getIdContentConfig() {
		return idContentConfig;
	}

	public void setIdContentConfig(Long idContentConfig) {
		this.idContentConfig = idContentConfig;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
