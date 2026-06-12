package com.htc.fleetmanagement.entity;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity {
	@Column(updatable = false)
	
	private LocalDateTime createdAt;
	
	@Column(insertable = false)
	
	private LocalDateTime updatedAt;

	
	public BaseEntity() {
		super();
	}

	public BaseEntity(LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	@PrePersist
	public void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	@PreUpdate
	public void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}
	
	

}

