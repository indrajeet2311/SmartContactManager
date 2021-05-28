package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Contact;
import com.example.demo.entities.User;

public interface ContactRepository extends JpaRepository<Contact,Integer>{
	
//pagination
	//"select u from Contact u where u.email = :email"
	//current page
	//Contactper page - 5
	
@Query("from Contact as c  where c.user.id =:userId")
public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pePageable);



public List<Contact> findByNameContainingAndUser(String name,User user);



}
