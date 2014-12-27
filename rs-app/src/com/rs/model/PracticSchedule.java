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
@Table(name = "practic_schedule")
public class PracticSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="polyclinic", referencedColumnName="polyclinic_code", nullable=false)
	private Polyclinic polyclinic;
	
	@ManyToOne
	@JoinColumn(name="doctor", nullable=false)
	private Employee doctor;
	
	@Column(name="days", length=10, nullable=false)
	private String days;
	
	@Temporal(TemporalType.TIME)
	@Column(name="start_time")
	private Date startTime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="end_time")
	private Date endTime;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Polyclinic getPolyclinic() {
		return polyclinic;
	}

	public void setPolyclinic(Polyclinic polyclinic) {
		this.polyclinic = polyclinic;
	}

	public Employee getDoctor() {
		return doctor;
	}

	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
