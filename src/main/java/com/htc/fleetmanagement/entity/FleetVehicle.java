package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.htc.fleetmanagement.util.VehicleStatus;

@Entity
@Table(name = "351_fleet_vehicle_351")
@NoArgsConstructor
@AllArgsConstructor
public class FleetVehicle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @JsonIgnoreProperties({"fleetVehicles", "employees", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private CorporateClient client;

    @Column(name = "vin", nullable = false, unique = true, length = 17)
    private String vin;

    @Column(name = "make_model", nullable = false, length = 255)
    private String makeModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private VehicleStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<FleetClaim> claims;



//	public FleetVehicle(CorporateClient client, String vin, String makeModel, VehicleStatus status) {
//		super();
//		this.client = client;
//		this.vin = vin;
//		this.makeModel = makeModel;
//		this.status = status;
//	}

	public Integer getVehicleId() {
		return vehicleId;
	}


	public CorporateClient getClient() {
		return client;
	}

	public void setClient(CorporateClient client) {
		this.client = client;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getMakeModel() {
		return makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public void setStatus(VehicleStatus status) {
		this.status = status;
	}


	public List<FleetClaim> getClaims() {
		return claims;
	}

	public void setClaims(List<FleetClaim> claims) {
		this.claims = claims;
	}

	@Override
	public String toString() {
		return "FleetVehicle [vehicleId=" + vehicleId + ", client=" + client + ", vin=" + vin + ", makeModel="
				+ makeModel + ", status=" + status + ", claims=" + claims + "]";
	}
    
    
    
}