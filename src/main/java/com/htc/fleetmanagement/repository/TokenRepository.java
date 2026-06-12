package com.htc.fleetmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	// Find all valid tokens for a user (not expired and not revoked)
	@Query("select t from Token t where t.user.userId = :userId and (t.isExpired = false and t.isRevoked = false)")
	List<Token> findAllValidTokensByUser(@Param("userId") Integer userId);
	
	
	Optional<Token> findByJwttoken(String jwttoken);
	
	// Find all tokens that are expired but not yet marked as expired
	@Query("select t from Token t where t.expiresAt < :now and t.isExpired = false")
	List<Token> findExpiredTokens(@Param("now") LocalDateTime now);
	
	// Find all tokens that are revoked but not yet marked as revoked
	@Query("select t from Token t where t.isExpired = true and t.isRevoked = true")
	List<Token> findRevokedTokens();
	
	// Mark tokens as expired if their expiration date has passed
	// Modifying query to update tokens in bulk
	@Modifying
	@Transactional
	@Query("update Token t set t.isExpired = true where t.expiresAt < :now")
	void markExpiredTokens(@Param("now") LocalDateTime now);
	
	// Mark tokens as revoked if they are expired and revoked
	@Modifying
	@Transactional
	@Query("delete from Token t where t.isExpired = true and t.isRevoked = true and t.expiresAt < :cutoffDate")
	void deleteRevokedTokensOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}