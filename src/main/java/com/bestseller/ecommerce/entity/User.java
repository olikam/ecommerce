package com.bestseller.ecommerce.entity;

import com.bestseller.ecommerce.entity.Cart;
import com.bestseller.ecommerce.model.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NotNull(message = "First Name cannot be empty.")
	private String firstName;

	@NotNull(message = "Last Name cannot be empty.")
	private String lastName;

	@NotNull(message = "Phone number cannot be empty.")
	@Column(unique=true)
	private String phoneNumber;

	@NotNull(message = "Email address cannot be empty.")
	@Email(message = "Enter a valid email address.")
	@Column(unique=true)
	private String username;

	@Enumerated(EnumType.STRING)
	private UserRole userRole = UserRole.USER;

	private String address;

	@NotNull(message = "Password cannot be empty.")
	private String password;

	private boolean enabled = true;

	@OneToOne
	@JsonBackReference
	private Cart cart;

	public User() {
	}

	public User(String firstName, String lastName, String phoneNumber, String username, UserRole userRole, String address, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.username = username;
		this.address = address;
		this.password = password;
		this.userRole = userRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

	public String getPassword() {
		return "{ldap}" + password;
	}

	@Override
	public String getUsername() {
		return username;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(username, user.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phoneNumber, username);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", username='" + username + '\'' + ", userRole=" + userRole + ", address='" + address + '\'' + ", password='" + password + '\'' + ", enabled=" + enabled + '}';
	}
}
