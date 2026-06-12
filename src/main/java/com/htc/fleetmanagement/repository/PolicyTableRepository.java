package com.htc.fleetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.fleetmanagement.entity.PolicyTable;


@Repository
public interface PolicyTableRepository extends JpaRepository<PolicyTable, String>{

}
