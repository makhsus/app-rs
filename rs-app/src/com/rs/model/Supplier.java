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

import org.hibernate.annotations.Type;

@Entity
@Table(name="supplier")
public class Supplier implements Serializable {
	private static final long serialVersionUID = 3448025803207298346L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="supplier_id")
	private Long supplierId;
	
	@Column(name="supplier_code", length=20, nullable = false, unique = true)
	private String supplierCode;
	
	@Column(name="supplier_name", nullable = false)
	private String supplierName;
	
	@Column(name="address_supplier")
	@Type(type="text")
	private String address;
	
	@Column(name="office_phone", length=25)
	private String officePhone;
	
	@Column(name="phone_supplier", length=25)
	private String phoneSupplier;
	
	@Column(name="fax_supplier", length=20)
	private String fax;
	
	@Column(name="email_supplier", length=80)
	private String email;
	
	@Column(name="website_supplier", length=100)
	private String website;
	
	@Column(name="is_active", nullable=false)
	private boolean isActive;
	
	@Column(name="created_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name="updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
		
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@ManyToOne
	@JoinColumn(name="city_id")
	private City cityid;
	
	@ManyToOne
	@JoinColumn(name="province_id")
	private Province provinceId;

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getPhoneSupplier() {
		return phoneSupplier;
	}

	public void setPhoneSupplier(String phoneSupplier) {
		this.phoneSupplier = phoneSupplier;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public City getCityid() {
		return cityid;
	}

	public void setCityid(City cityid) {
		this.cityid = cityid;
	}

	public Province getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Province provinceId) {
		this.provinceId = provinceId;
	}
	
}
