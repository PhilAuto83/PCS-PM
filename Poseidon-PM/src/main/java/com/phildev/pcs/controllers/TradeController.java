package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.service.TradeService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class TradeController {

    private static final Logger logger  = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model, Principal connectedUser)
    {
        if(tradeService.findAll().isEmpty()){
            model.addAttribute("emptyTrades", "You have currently no trades");
        }
        model.addAttribute("trades", tradeService.findAll());
        model.addAttribute("connectedUser", connectedUser.getName());
        logger.info("Trade list page rendered successfully to user {}", connectedUser.getName());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Model model, Principal connectedUser) {
        Trade trade = new Trade();
        model.addAttribute("trade", trade);
        model.addAttribute("connectedUser", connectedUser.getName());
        logger.info("Trade add page rendered to user {}", connectedUser.getName());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rule name {}", errors);
            return "trade/add";
        }else if(!tradeService.checkBidExistWithAccountQuantityAndType(trade)){
            model.addAttribute("tradeError", "No bid found for this account, type and quantity");
            logger.error("No bid found for the account {} , type {} and quantity {}", trade.getAccount(), trade.getType(), trade.getBuyQuantity());
            return "trade/add";
        }else{
            Trade tradeSaved = tradeService.save(trade);
            logger.info("Trade of type  {} with buy quantity {} was successfully saved", tradeSaved.getType(), tradeSaved.getBuyQuantity());
            return "redirect:/trade/list";
        }
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a rule name {}", errors);
            return "trade/add";
        }else if(!tradeService.checkBidExistWithAccountQuantityAndType(trade)){
            model.addAttribute("tradeError", "No bid found for this account, type and quantity");
            logger.error("No bid found for the account {} , type {} and quantity {}", trade.getAccount(), trade.getType(), trade.getBuyQuantity());
            return "trade/add";
        }else{
            Trade tradeSaved = tradeService.save(trade);
            logger.info("Trade of type  {} with buy quantity {} was successfully updated", tradeSaved.getType(), tradeSaved.getBuyQuantity());
            return "redirect:/trade/list";
        }
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeService.delete(trade.getTradeId());
        model.addAttribute("trades", tradeService.findAll());
        return "redirect:/trade/list";
    }
}
