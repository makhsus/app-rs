package com.rs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "medical_trn")
public class MedicalTransaction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="status", length=50, nullable = false)
	private String status;
	
	@Column(name="registration_no", unique=true, nullable=false)
	private String registrationNo;
	
	@ManyToOne
	@JoinColumn(name="patient", nullable=false)
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name="poly_id")
	private Polyclinic poly;
	
	@ManyToOne
	@JoinColumn(name="doctor_id")
	private Employee doctor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="reg_date", nullable=false)
	private Date regDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="check_in_date", nullable=false)
	private Date checkInDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="check_out_date", nullable=false)
	private Date checkOutDate;
	
	@OneToOne
	@JoinColumn(name="room_id", nullable=false)
	private Room room;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private Users createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date", nullable = false)
	private Date createdDate;
	
}
