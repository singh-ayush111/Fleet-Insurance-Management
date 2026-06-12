package com.htc.fleetmanagement.dto;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.entity.User;

public class UserPrincipal implements UserDetails {
	
	private UserDetails userDetails;
	
	public UserPrincipal(UserDetails userDetails) {
		super();
		this.userDetails = userDetails;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userDetails.getAuthorities();
	}

	@Override
	public @Nullable String getPassword() {
		return userDetails.getPassword();
	}

	@Override
	public String getUsername() {
		return userDetails.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return userDetails.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return userDetails.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return userDetails.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return userDetails.isEnabled();
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		if (userDetails instanceof Employee) {
			return ((Employee) userDetails).getEmployeeId();
		} else if (userDetails instanceof User) {
			return ((User) userDetails).getUserId();
		}
		return null;
	}
	

	public UserDetails getUserDetails() {
		return userDetails;
	}
}