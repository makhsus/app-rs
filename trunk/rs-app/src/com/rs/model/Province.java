package com.rs.model;

/*
 *  candra.assasin@gmail.com
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="provinces")
public class Province implements Serializable {
	private static final long serialVersionUID = 3448025803207298346L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "province_id")
	private Integer provinceId;
	
	@Column (name="diknas_code", length=10)
	private String diknasCode;
	
	@Column (name="bps_code",length=10 )
	private String bpsCode;
	
	@Column (name="province_name",length=50 )
	private String provinceName;
	
	@Column (name="status",length=10 )
	private String status;

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getDiknasCode() {
		return diknasCode;
	}

	public void setDiknasCode(String diknasCode) {
		this.diknasCode = diknasCode;
	}

	public String getBpsCode() {
		return bpsCode;
	}

	public void setBpsCode(String bpsCode) {
		this.bpsCode = bpsCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
