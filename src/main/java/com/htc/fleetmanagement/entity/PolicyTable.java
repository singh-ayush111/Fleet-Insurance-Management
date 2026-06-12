package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "351_policy_table")
public class PolicyTable {

    @Id
    @Column(name = "master_policy_number", length = 100, unique = true)
    private String masterPolicyNumber;

    @Column(name = "policy_name", nullable = false, length = 255)
    private String policyName;

    @Column(name = "premium", nullable = false, precision = 10, scale = 2)
    private BigDecimal premium;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @JsonIgnore
    @OneToMany(mappedBy = "masterPolicy")  
    private List<CorporateClient> corporateClients;

    public PolicyTable() { 
    }

    public String getMasterPolicyNumber() { return masterPolicyNumber; }
    public void setMasterPolicyNumber(String masterPolicyNumber) { this.masterPolicyNumber = masterPolicyNumber; }

    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }

    public BigDecimal getPremium() { return premium; }
    public void setPremium(BigDecimal premium) { this.premium = premium; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public List<CorporateClient> getCorporateClients() { return corporateClients; }
    public void setCorporateClients(List<CorporateClient> corporateClients) { this.corporateClients = corporateClients; }

    @Override
    public String toString() {
        return "PolicyTable [masterPolicyNumber=" + masterPolicyNumber + ", policyName=" + policyName + 
               ", premium=" + premium + ", benefits=" + benefits + "]";
    }
}
