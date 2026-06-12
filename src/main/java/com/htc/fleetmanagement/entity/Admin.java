package com.htc.fleetmanagement.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "351_Admin_351")
@PrimaryKeyJoinColumn(name = "adminId",referencedColumnName = "userId"  )
@Getter
@Setter
public class Admin extends User {
	
}
