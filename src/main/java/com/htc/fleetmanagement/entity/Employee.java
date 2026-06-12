package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.htc.fleetmanagement.util.Role;

@Entity
@Table(name = "351_employee_351")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends BaseEntity implements UserDetails{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;
    
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    
    @Column(name = "username", nullable = false, length = 255, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @JsonIgnoreProperties({"fleetVehicles", "employees", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private CorporateClient client;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "license_number", nullable = false, length = 100, unique = true)
    private String licenseNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable=false, length = 50)
    private Role role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add "ROLE_" prefix to enum name
        String roleWithPrefix = "ROLE_" + role.name();
        return List.of(new SimpleGrantedAuthority(roleWithPrefix));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}