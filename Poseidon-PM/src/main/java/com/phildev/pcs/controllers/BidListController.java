package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.service.BidListService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class BidListController {

    private static final Logger logger = LoggerFactory.getLogger(BidListController.class);

    @Autowired
    private BidListService bidListService;

    @GetMapping("/bidList/list")
    public String home(Model model, Principal connectedUser)
    {
        List<BidList> allBids = bidListService.findAll();
        model.addAttribute("connectedUser", connectedUser.getName());
        model.addAttribute("bidLists", allBids);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model, Principal connectedUser) {
        BidList bidList = new BidList();
        model.addAttribute("bidList", bidList);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a bid {}", errors);
            return "bidList/add";
        }
        BidList bidSaved = bidListService.save(bid);
        logger.info("Bid  fro account {} was saved successfully for user {}", bidSaved.getAccount(), connectedUser.getName());
        model.addAttribute("connectedUser", connectedUser.getName());
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        BidList bidList = bidListService.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid bid id :"+id));
        model.addAttribute("bidList", bidList);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a bid {}", errors);
            return "bidList/add";
        }
        bidList.setBidListId(id);
        BidList bidUpdated = bidListService.save(bidList);
        logger.info("Bid was saved successfully with current id {} for user {}", bidUpdated.getBidListId(), connectedUser.getName());

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        BidList bid = bidListService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        bidListService.delete(id);
        model.addAttribute("trades", bidListService.findAll());
        logger.info("Bid was deleted successfully for id {} and user {}", id, connectedUser.getName());
        return "redirect:/bidList/list";
    }
}
