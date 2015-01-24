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
@Table(name="medicament_purchases_items")
public class MedicamentPurchasesItems implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="mp_item_id")
	private Long mpItemId;
	
	@ManyToOne
	@JoinColumn(name="purchase", nullable=false)
	private MedicamentPurchases purchase;
	
	@ManyToOne
	@JoinColumn(name="product")
	private Medicament product;
	
	@Column(name="product_code", length=50)
	private String productCode;
	
	@Column(name="product_name", nullable=false)
	private String productName;
	
	@Column(name="expire_date")
	@Temporal(TemporalType.DATE)
	private Date expireDate;
	
	@Column(name="pack_unit", length=20)
	private String packUnit;
	
	@Column(name="pack_quantity", length=10)
	private int packQuantity;
	
	@Column(name="quantity_per_pack", length=10)
	private int quantityPerPack;
	
	@Column(name="quantity_unit", length=20)
	private String quantityUnit;
	
	@Column(name="p_unit_price", nullable=false)
	private BigDecimal priceUnit;
	
	@Column(name="p_unit_total_price")
	private BigDecimal priceUnitTotal; //quantity_per_pack  * priceUnit
	
	@Column(name="ppn_persen")
	private int ppnPersen;
	
	@Column(name="ppn_amount")
	private BigDecimal ppnAmount;
	
	@Column(name="buy_price")
	private BigDecimal priceBuy;
	
	@Column(name="markup_persen")
	private int markupPersen;
	
	@Column(name="markup_amount")
	private BigDecimal markupAmount;
	
	@Column(name="sell_price")
	private BigDecimal priceSell;
	
	@Column(name="retail_price")
	private BigDecimal priceRetail;

	
	
	public Long getMpItemId() {
		return mpItemId;
	}
	
	public void setMpItemId(Long mpItemId) {
		this.mpItemId = mpItemId;
	}

	public MedicamentPurchases getPurchase() {
		return purchase;
	}

	public void setPurchase(MedicamentPurchases purchase) {
		this.purchase = purchase;
	}

	public Medicament getProduct() {
		return product;
	}

	public void setProduct(Medicament product) {
		this.product = product;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public int getPackQuantity() {
		return packQuantity;
	}
	
	public void setPackQuantity(int packQuantity) {
		this.packQuantity = packQuantity;
	}

	public int getQuantityPerPack() {
		return quantityPerPack;
	}

	public void setQuantityPerPack(int quantityPerPack) {
		this.quantityPerPack = quantityPerPack;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public BigDecimal getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(BigDecimal priceUnit) {
		this.priceUnit = priceUnit;
	}

	public BigDecimal getPriceUnitTotal() {
		return priceUnitTotal;
	}

	public void setPriceUnitTotal(BigDecimal priceUnitTotal) {
		this.priceUnitTotal = priceUnitTotal;
	}

	public int getPpnPersen() {
		return ppnPersen;
	}

	public void setPpnPersen(int ppnPersen) {
		this.ppnPersen = ppnPersen;
	}

	public BigDecimal getPpnAmount() {
		return ppnAmount;
	}

	public void setPpnAmount(BigDecimal ppnAmount) {
		this.ppnAmount = ppnAmount;
	}

	public BigDecimal getPriceBuy() {
		return priceBuy;
	}

	public void setPriceBuy(BigDecimal priceBuy) {
		this.priceBuy = priceBuy;
	}

	public int getMarkupPersen() {
		return markupPersen;
	}

	public void setMarkupPersen(int markupPersen) {
		this.markupPersen = markupPersen;
	}

	public BigDecimal getMarkupAmount() {
		return markupAmount;
	}

	public void setMarkupAmount(BigDecimal markupAmount) {
		this.markupAmount = markupAmount;
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
	
}
