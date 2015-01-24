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
@Table(name = "medicament")
public class Medicament implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="medicament_id")
	private Long medicamentId;

	@Column(name="medicament_code", length=20, unique=true, nullable=false)
	private String medicamentCode;
	
	@Column(name="medicament_name", nullable=false)
	private String medicamentName;
	
	@ManyToOne
	@JoinColumn(name="supplier", referencedColumnName="supplier_id")
	private Supplier supplier;
	
	@Column(name="medicament_unit", length=20, nullable=false)
	private String medicamentUnit;
	
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
	
	
	

	public Long getMedicamentId() {
		return medicamentId;
	}

	public void setMedicamentId(Long medicamentId) {
		this.medicamentId = medicamentId;
	}

	public String getMedicamentCode() {
		return medicamentCode;
	}

	public void setMedicamentCode(String medicamentCode) {
		this.medicamentCode = medicamentCode;
	}

	public String getMedicamentName() {
		return medicamentName;
	}

	public void setMedicamentName(String medicamentName) {
		this.medicamentName = medicamentName;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getMedicamentUnit() {
		return medicamentUnit;
	}

	public void setMedicamentUnit(String medicamentUnit) {
		this.medicamentUnit = medicamentUnit;
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
