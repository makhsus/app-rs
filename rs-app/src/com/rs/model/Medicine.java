package com.rs.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "medicine")
public class Medicine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="medicine_id")
	private Long medicinetId;

	@Column(name="medicine_code", length=20, unique=true, nullable=false)
	private String medicineCode;
	
	@Column(name="medicine_name", nullable=false)
	private String medicineName;
	
	@ManyToOne
	@JoinColumn(name="supplier")
	private Supplier supplier;
	
	@Column(name="medicine_unit", length=20, nullable=false)
	private String medicineUnit;
	
	@Column(name="available_stock", nullable=false)
	private Integer availableStock;
	
	@Column(name="price_buy")
	private BigDecimal priceBuy;
	
	@Column(name="price_sell")
	private BigDecimal priceSell;
	
	@Column(name="price_retail")
	private BigDecimal priceRetail;
	
	@Column(name="updated_price_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedPriceDate;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Column(name="created_date", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name="updated_by")
	private Users updatedBy;
	
	@Column(name="updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	
	
	public Long getMedicinetId() {
		return medicinetId;
	}

	public void setMedicinetId(Long medicinetId) {
		this.medicinetId = medicinetId;
	}

	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getMedicineUnit() {
		return medicineUnit;
	}

	public void setMedicineUnit(String medicineUnit) {
		this.medicineUnit = medicineUnit;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public BigDecimal getPriceBuy() {
		return priceBuy;
	}

	public void setPriceBuy(BigDecimal priceBuy) {
		this.priceBuy = priceBuy;
	}

	public BigDecimal getPriceSell() {
		return priceSell;
	}

	public void setPriceSell(BigDecimal priceSell) {
		this.priceSell = priceSell;
	}

	public BigDecimal getPriceRetail() {
		return priceRetail;
	}

	public void setPriceRetail(BigDecimal priceRetail) {
		this.priceRetail = priceRetail;
	}

	public Date getUpdatedPriceDate() {
		return updatedPriceDate;
	}

	public void setUpdatedPriceDate(Date updatedPriceDate) {
		this.updatedPriceDate = updatedPriceDate;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Users getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Users updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
