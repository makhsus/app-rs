package com.rs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="cities")
public class City implements Serializable {
	private static final long serialVersionUID = 3448025803207298346L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "city_id")
	private Integer cityId;
	
	@Column (name="diknas_code", length=10)
	private String diknasCode;
	
	@Column (name="bps_code",length=10 )
	private String bpsCode;
	
	@Column (name="city_name",length=50 )
	private String cityName;
		
	@Column (name="status",length=10 )
	private String status;
	
	@ManyToOne
	@JoinColumn(name="province_id", referencedColumnName = "province_id")
	private Province provinces;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Province getProvinces() {
		return provinces;
	}

	public void setProvinces(Province provinces) {
		this.provinces = provinces;
	}
	
	

}
