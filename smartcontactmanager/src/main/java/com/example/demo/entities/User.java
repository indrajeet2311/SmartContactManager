package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int id;
@NotBlank(message= "Name field is required!!")
@Size(min=2,max =20, message= "min 2 and max 20 characters are required")
private String name;
@Column(unique = true)
private String email;
private String password;
private boolean enabled;
private String imageUrl;
@Column(length = 200)
private String about;

public String Role;

public String getRole() {
	return Role;
}






public void setRole(String role) {
	Role = role;
}


@OneToMany(cascade=CascadeType.ALL,fetch =FetchType.LAZY,orphanRemoval=true,mappedBy ="user")
private List<Contact> contacts = new ArrayList<>();


public User()
{
}




@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", enabled=" + enabled
			+ ", imageUrl=" + imageUrl + ", about=" + about + ", Role=" + Role + ", contacts=" + contacts + "]";
}






public List<Contact> getContacts() {
	return contacts;
}




public void setContacts(List<Contact> contacts) {
	this.contacts = contacts;
}




public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
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
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}











}
