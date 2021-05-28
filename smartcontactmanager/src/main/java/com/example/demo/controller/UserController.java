package com.example.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.ContactRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.entities.Contact;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

@Autowired
private UserRepository userRepository;

@Autowired
private ContactRepository contactRepository;

@Autowired
private BCryptPasswordEncoder bCryptPasswordEncoder;

@ModelAttribute
public void addCommonData(Model m, 	Principal principal)
{
	String userName = principal.getName();
	System.out.println("USERNAME" +" " + userName);
	
	User user = userRepository.getUserByUserName(userName);
	
	System.out.println("USER" + user);
	
	m.addAttribute("user", user);	
}
	
@RequestMapping("/index")
public String dashboard(Model model, Principal principal)
{
	
	
	return "normal/user_dashboard";
}
// add form handler
@GetMapping("/add-contact")
public String openAddContactForm(Model model)
{
	model.addAttribute("title","Add Contact");
	model.addAttribute("contact", new Contact());
	return "normal/new_contact_form";
}

@PostMapping("/process-contact")
public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,HttpSession session)
{
	try{
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		
		
		//processing and uploading the files
		
		if(file.isEmpty())
		{
			contact.setImage("add-user.png");
			
			// if the file is empt y then message
		}
		else {
			// upoad it in the folder
			contact.setImage(file.getOriginalFilename());
			
			File file1= new ClassPathResource("static/img").getFile();
			Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename() );
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			
		}
		
		
		contact.setUser(user);
		
		
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		System.out.println(contact);
		
		session.setAttribute("message", new Message("Your Contact is added", "success"));
		
	}
	catch(Exception e)
	{
		System.out.println("ERROR" +"  " + e.getMessage());
		e.printStackTrace();
		session.setAttribute("message", new Message("Something went wrong", "danger"));
	}
	
	return "normal/new_contact_form";
}

//per page  = 5n
//current page = 0 
@GetMapping("/show-contacts/{page}")
public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal)
{
	m.addAttribute("title","Show User Contacts");
	
	//contact ki list
	
	String userName = principal.getName();
	
	User user = this.userRepository.getUserByUserName(userName);
	
	Pageable pageable = PageRequest.of(page, 5);
	
	Page<Contact> contacts =  this.contactRepository.findContactsByUser(user.getId(), pageable);
	
	m.addAttribute("contacts", contacts);
	
	m.addAttribute("currentpage",page);
	
	m.addAttribute("totalpages",contacts.getTotalPages());
	
	
	
	
	return "normal/show_contacts";
}

@RequestMapping("/{cId}/contact")
public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal)
{
	System.out.println(cId);
	
	Optional<Contact> contactOptional = this.contactRepository.findById(cId);
	Contact contact  = contactOptional.get();
	
	
	String username = principal.getName();
	User user  = this.userRepository.getUserByUserName(username);
	
	if(user.getId() == contact.getUser().getId())
	{
		model.addAttribute("contact",contact);
	}
	
	
	
	return "normal/contact_detail";
}


//delete contact
@GetMapping("/delete/{cid}")
@Transactional
public String deleteContact(@PathVariable("cid") Integer cId,HttpSession session,Principal principal)
{
	Optional<Contact> contactOptional = this.contactRepository.findById(cId);
	
	Contact contact = contactOptional.get();
	
	//contact.setUser(null);
	
	//remove Image
	
	User user = this.userRepository.getUserByUserName(principal.getName());
	
	user.getContacts().remove(contact);
	
	this.userRepository.save(user);
	
	
	
	this.contactRepository.delete(contact);
	
	session.setAttribute("message", new Message("Contact deleted successfully", "success"));
	
	return "redirect:/user/show-contacts/0";
}


//update contact
@PostMapping("/update-contact/{cid}")
public String updateForm(@PathVariable("cid") Integer cid, Model m)
{
	m.addAttribute("title", "Update Contact");
	
	
	Contact contact = this.contactRepository.findById(cid).get();
	
	m.addAttribute("contact" , contact);
	
	return "normal/update_form";
}

//update contact handler
@RequestMapping(value="/process-update", method= RequestMethod.POST)
public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,HttpSession session,Principal principal,Model m)
{
	try {
		
	Contact oldcontactDetail = this.contactRepository.findById(contact.getcId()).get();
		if(!file.isEmpty())
		{
			File deleteFile = new ClassPathResource("static/img").getFile();
			File file1 = new File(deleteFile,oldcontactDetail.getImage());
			file1.delete();
			
			
			File saveFile= new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename() );
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(file.getOriginalFilename());
			
		}else {
			contact.setImage(oldcontactDetail.getImage());
		}
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		
		System.out.println("eeeE"+user +"Ddedeeeee");
		contact.setUser(user);
		
		this.contactRepository.save(contact);
	  
		session.setAttribute("message", new Message("Your contact is Updated","success" ));
		
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	
	System.out.println(contact.getName());
	System.out.println(contact.getcId());
	
	return "redirect:/user/" + contact.getcId() + "/contact";
}


//your profile handler

@GetMapping("/profile")
public String yourProfile(Model model)
{
model.addAttribute("title","Profile Page");
return "normal/profile";
	
}


//open settings
@GetMapping("/settings")
public String openSettings()
{
	return "normal/settings";
}

//change password handler
@PostMapping("change-password")
public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Principal principal,HttpSession session)
{
	
	String username = principal.getName();
	User currentUser  = this.userRepository.getUserByUserName(username);
	
	if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
	{
		
		currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(currentUser);
		session.setAttribute("message", new Message("Your Password changed successfully","success"));
	}
	else {
		session.setAttribute("message", new Message("Please enter Correct Old Password ","danger"));
	 return "redirect:/user/settings/";
	}
	System.out.println("old" + oldPassword);
	System.out.println("new" + newPassword);
	
	
	
	
	return "redirect:/user/index/";
}







	
}
