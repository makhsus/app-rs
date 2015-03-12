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
@Table(name = "users")
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_user")
	private Long idUser;
	
	@Column(name="user_name", length=50, nullable = false, unique = true)
	private String userName;
	
	@Column(name="user_password", length=80, nullable = false)
	private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_update")
	private Date lastUpdate;
	
	@Column(name="status", length=1, nullable = false)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login")
	private Date lastLogin;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_logout")
	private Date lastLogout;
	
	@ManyToOne
	@JoinColumn(name="id_employee", referencedColumnName="id_employee", nullable=false)
	private Employee idEmployee;
	
	@ManyToOne
	@JoinColumn(name="id_sub_role", referencedColumnName="id_sub_role", nullable=false)
	private SubRoles idSubRole;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(Date lastLogout) {
		this.lastLogout = lastLogout;
	}

	public Employee getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(Employee idEmployee) {
		this.idEmployee = idEmployee;
	}

	public SubRoles getIdSubRole() {
		return idSubRole;
	}

	public void setIdSubRole(SubRoles idSubRole) {
		this.idSubRole = idSubRole;
	}
	
	
}
