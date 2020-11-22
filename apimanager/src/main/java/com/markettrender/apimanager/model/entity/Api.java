package com.markettrender.apimanager.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "apis")
public class Api implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "api_name", unique = true)
	private String name;

	@Column(name = "number_of_petitions")
	private long numberOfPetitions;

	@NotNull
	@Column(name = "max_petitions")
	private long maxPetitions;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	@Column(name = "last_petition")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastPetition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMaxPetitions() {
		return maxPetitions;
	}

	public void setMaxPetitions(long maxPetitions) {
		this.maxPetitions = maxPetitions;
	}

	public Date getLastPetition() {
		return lastPetition;
	}

	public void setLastPetition(Date lastPetition) {
		this.lastPetition = lastPetition;
	}

	public Long getNumberOfPetitions() {
		return numberOfPetitions;
	}

	public void setNumberOfPetitions(Long numberOfPetitions) {
		this.numberOfPetitions = numberOfPetitions;
	}

}
