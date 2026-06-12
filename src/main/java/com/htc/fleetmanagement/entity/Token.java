package com.htc.fleetmanagement.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;

@Entity
@Table(name = "351_token_351")

public class Token {
	@Id
	@GeneratedValue
	private Long id;
	public Token() {
		super();
	}

	@Column(columnDefinition = "LONGTEXT")
	private String jwttoken;
	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	private boolean isRevoked;
	private boolean isExpired;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expires_at")
	private LocalDateTime expiresAt;
	
	public Token(String token, boolean isRevoked, boolean isExpired, User user) {
		super();
		this.jwttoken = token;
		this.isRevoked = isRevoked;
		this.isExpired = isExpired;
		this.user = user;
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", Token=" + jwttoken + ", isRevoked=" + isRevoked + ", isExpired=" + isExpired 
				+ ", createdAt=" + createdAt + ", expiresAt=" + expiresAt + ", user=" + user + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isRevoked() {
		return isRevoked;
	}

	public void setRevoked(boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
