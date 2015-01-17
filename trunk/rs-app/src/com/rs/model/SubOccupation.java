package com.rs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "sub_occupation")
public class SubOccupation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sub_occupation_id")
	private Long subOccupationId;
	
	@Column(name="sub_occupation_code", length=20, nullable=false, unique=true)
	private String subOccupationCode;
	
	@Column(name="sub_occupation_name", length=100, nullable=false)
	private String subOccupationName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;
	
	@ManyToOne
	@JoinColumn(name="occupation")
	private Occupation occupationId;
	
	@Column(name="status", length=1, nullable = false)
	private String status;

	public Long getSubOccupationId() {
		return subOccupationId;
	}

	public void setSubOccupationId(Long subOccupationId) {
		this.subOccupationId = subOccupationId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSubOccupationCode() {
		return subOccupationCode;
	}

	public void setSubOccupationCode(String subOccupationCode) {
		this.subOccupationCode = subOccupationCode;
	}

	public String getSubOccupationName() {
		return subOccupationName;
	}

	public void setSubOccupationName(String subOccupationName) {
		this.subOccupationName = subOccupationName;
	}

	public Occupation getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Occupation occupationId) {
		this.occupationId = occupationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
