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
@Table(name = "employee")
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_employee")
	private Long idEmployee;
	
	@Column(name="NIK", nullable = false, length=200, unique = true)
	private String nik;
	
	@Column(name="full_name", length=200, nullable = false)
	private String fullName;
	
	@Type(type="text")
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="gender", length=1, nullable = false)
	private String gender;
	
	@Temporal(TemporalType.DATE)
	@Column(name="birthdate", nullable = false)
	private Date birthdate;
	
	@Column(name="status", length=1, nullable = false)
	private String status;
	
	@ManyToOne
	@JoinColumn(name="city_id", referencedColumnName="city_id", nullable=false)
	private City cityid;
	
	@ManyToOne
	@JoinColumn(name="province_id", referencedColumnName="province_id", nullable=false)
	private Province provinceId;
	
	@Column(name="email", length=100)
	private String email;
	
	@Column(name="phone_number", length=30)
	private String phoneNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="register_date")
	private Date registerDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

}
