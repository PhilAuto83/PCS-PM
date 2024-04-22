package com.phildev.pcs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	/**
	 * This method displays the home page which has a link to login page or user list endpoint
	 * @param model
	 * @return the home.html page
	 */
	@GetMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	/**
	 * This method is calling /admin/home and redirects to bidList/list view which is the list of all bids in the app
	 * @param model
	 * @return the bidList/list view by calling /bidList/list endpoint
	 */
	@GetMapping("/admin/home")
	public String adminHome(Model model)
	{

		return "redirect:/bidList/list";
	}


}
