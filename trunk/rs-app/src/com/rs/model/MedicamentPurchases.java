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
@Table(name="medicament_purchases")
public class MedicamentPurchases implements Serializable {
private static final long serialVersionUID = 3448025803207298346L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="m_purchases_id")
	private Long mdcPurchasesId;
	
	@ManyToOne
	@JoinColumn(name="supplier")
	private Supplier supplier;

	@Column(name="invoice_no", length=20)
	private String invoiceNo;

	@Column(name="purchase_date", nullable=false)
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;
	
	@Column(name="amount_item", nullable=false)
	private BigDecimal amountItem;
	
	@Column(name="amount_discount")
	private BigDecimal amountDiscount;
	
	@Column(name="amount_ppn")
	private BigDecimal amountPpn;
	
	@Column(name="amount_invoice", nullable=false)
	private BigDecimal amountInvoice;
	
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
	
	

	public Long getMdcPurchasesId() {
		return mdcPurchasesId;
	}
	
	public void setMdcPurchasesId(Long mdcPurchasesId) {
		this.mdcPurchasesId = mdcPurchasesId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public BigDecimal getAmountItem() {
		return amountItem;
	}

	public void setAmountItem(BigDecimal amountItem) {
		this.amountItem = amountItem;
	}

	public BigDecimal getAmountDiscount() {
		return amountDiscount;
	}

	public void setAmountDiscount(BigDecimal amountDiscount) {
		this.amountDiscount = amountDiscount;
	}

	public BigDecimal getAmountPpn() {
		return amountPpn;
	}

	public void setAmountPpn(BigDecimal amountPpn) {
		this.amountPpn = amountPpn;
	}

	public BigDecimal getAmountInvoice() {
		return amountInvoice;
	}

	public void setAmountInvoice(BigDecimal amountInvoice) {
		this.amountInvoice = amountInvoice;
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
