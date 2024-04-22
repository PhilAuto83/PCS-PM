package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.service.RuleNameService;
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

    /**
     * This method is displaying trade/list view when calling endpoint /trade/list
     * @param model which is sending user connected and trades to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return trade/list view
     */
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

    /**
     * This method is displaying trade/add view when calling endpoint /trade/add
     * @param model which is sending user connected and  a single {@link Trade} object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return trade/add view
     */
    @GetMapping("/trade/add")
    public String addUser(Model model, Principal connectedUser) {
        Trade trade = new Trade();
        model.addAttribute("trade", trade);
        model.addAttribute("connectedUser", connectedUser.getName());
        logger.info("Trade add page rendered to user {}", connectedUser.getName());
        return "trade/add";
    }

    /**
     * This method is validating {@link Trade} object and saving it in database through {@link TradeService#save(Trade)}
     * @param trade which is a {@link Trade} object
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return trade/add if there is an error or redirect to /trade/list endpoint to display new trade list with freshly added trade
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a trade  {}", errors);
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

    /**
     * This method is displaying trade/update view with a trade retrieved by its id through {@link TradeService#findById(Integer)}
     * @param id which is the path variable to retrieve trade by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return trade/update view
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        model.addAttribute("connectedUser", connectedUser.getName());
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    /**
     * This method is used to save a trade which has been updated and retrieved by its id
     * @param id which is the path variable to retrieve trade by id
     * @param trade which is the {@link Trade} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the trade/add view if there is an error or the trade/list if the update is successful
     */

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a trade name {}", errors);
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

    /**
     * This method is deleting a trade by its id by using the service {@link TradeService#delete(Integer)}
     * @param id which is the path variable to retrieve trade by id
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @return the trade/list view after calling /trade/list endpoint
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeService.delete(trade.getTradeId());
        model.addAttribute("trades", tradeService.findAll());
        return "redirect:/trade/list";
    }
}
