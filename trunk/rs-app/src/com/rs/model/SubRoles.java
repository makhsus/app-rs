package com.rs.model;

/*
 *  candra.assasin@gmail.com
 */

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
@Table(name = "sub_roles")
public class SubRoles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_sub_role")
	private Long idSubRole;
	
	@Column(name="sub_role_name", length=150)
	private String subRoleName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_update")
	private Date lastUpdate;
	
	@Type(type="text")
	@Column(name="description")
	private String description;
	
	@Column(name="status", length=1, nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name="id_role", referencedColumnName="id_role", nullable=false)
	private Roles idRole;

	public Long getIdSubRole() {
		return idSubRole;
	}

	public void setIdSubRole(Long idSubRole) {
		this.idSubRole = idSubRole;
	}

	public String getSubRoleName() {
		return subRoleName;
	}

	public void setSubRoleName(String subRoleName) {
		this.subRoleName = subRoleName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Roles getIdRole() {
		return idRole;
	}

	public void setIdRole(Roles idRole) {
		this.idRole = idRole;
	}
	
	
}
