package com.htc.fleetmanagement.entity;

import com.htc.fleetmanagement.util.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "351_insurance_agent_351")
@PrimaryKeyJoinColumn(name = "agentId",referencedColumnName = "userId"  )
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InsuranceAgent extends User {

	@Enumerated(EnumType.STRING)
    @Column(name = "accountStatus", nullable = false, length = 50)
    private AccountStatus status;

	


	public AccountStatus getStatus() {
		return status;
	}
		

}
