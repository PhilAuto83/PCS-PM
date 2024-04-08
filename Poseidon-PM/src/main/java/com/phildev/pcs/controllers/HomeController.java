package com.phildev.pcs.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;

@Controller
public class HomeController
{
	@GetMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	@GetMapping("/admin/home")
	public String adminHome(Model model)
	{

		return "redirect:/bidList/list";
	}


}
