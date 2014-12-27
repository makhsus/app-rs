package com.rs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="polyclinic")
public class Polyclinic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="polyclinic_id")
	private Long polyclinicId;
	
	@Column(name="polyclinic_code", nullable=false, unique=true)
	private String polyclinicCode;
	
	@Column(name="polyclinic_name", nullable=false)
	private String polyclinicName;
	
	@Column(name="is_active", nullable = false)
	private boolean isActive;
	

	public Long getPolyclinicId() {
		return polyclinicId;
	}

	public void setPolyclinicId(Long polyclinicId) {
		this.polyclinicId = polyclinicId;
	}
	
	public String getPolyclinicCode() {
		return polyclinicCode;
	}
	
	public void setPolyclinicCode(String polyclinicCode) {
		this.polyclinicCode = polyclinicCode;
	}

	public String getPolyclinicName() {
		return polyclinicName;
	}

	public void setPolyclinicName(String polyclinicName) {
		this.polyclinicName = polyclinicName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
