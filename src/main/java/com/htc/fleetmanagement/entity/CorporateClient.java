package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "351_corporate_client")
@PrimaryKeyJoinColumn(name = "client_id", referencedColumnName = "userId") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateClient extends User {

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "contact_email", nullable = false, length = 255)
    private String contactEmail;

    @JsonIgnoreProperties({"corporateClients", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "master_policy_number",
        referencedColumnName = "master_policy_number"
    )
    private PolicyTable masterPolicy;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FleetVehicle> fleetVehicles;

    @JsonIgnore
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    @Formula("(SELECT COUNT(fv.vehicle_id) FROM 351_fleet_vehicle_351 fv WHERE fv.client_id = client_id)")
    private Integer vehicleCount;

    
    @Formula("""
        (
            SELECT COUNT(fv.vehicle_id) * COALESCE(pt.premium, 0)
            FROM 351_fleet_vehicle_351 fv
            JOIN 351_policy_table pt ON pt.master_policy_number = (
                SELECT cc.master_policy_number
                FROM 351_corporate_client cc
                WHERE cc.client_id = client_id
            )
            WHERE fv.client_id = client_id
        )
    """)
    private BigDecimal totalPremium;
}