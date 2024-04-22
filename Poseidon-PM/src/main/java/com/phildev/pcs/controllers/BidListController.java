package com.phildev.pcs.controllers;

import com.phildev.pcs.domain.BidList;
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

    /**
     * This method is displaying bidList/list view when calling endpoint /bidList/list
     * @param model which is sending user connected and bidLists to the view to be displayed
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return bidList/list view
     */
    @GetMapping("/bidList/list")
    public String home(Model model, Principal connectedUser)
    {
        List<BidList> allBids = bidListService.findAll();
        model.addAttribute("connectedUser", connectedUser.getName());
        model.addAttribute("bidLists", allBids);
        return "bidList/list";
    }

    /**
     * This method is displaying bidList/add view when calling endpoint /bidList/add
     * @param model which is sending user connected and  a single bidList object to the view to be used in the form
     * @param connectedUser which is using {@link Principal} to retrieve the connected user and sends it to the model
     * @return bidList/add view
     */
    @GetMapping("/bidList/add")
    public String addBidForm(Model model, Principal connectedUser) {
        BidList bidList = new BidList();
        model.addAttribute("bidList", bidList);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "bidList/add";
    }

    /**
     * This method is validating {@link BidList} object and saving it in database through {@link BidListService#save(BidList)}
     * @param bid which is a {@link BidList} object 
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return bidList/add if there is an error or redirect to /bidList/list endpoint to display new bid list with freshly added bid
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a bid {}", errors);
            return "bidList/add";
        }
        BidList bidSaved = bidListService.save(bid);
        logger.info("Bid  for account {} was saved successfully for user {}", bidSaved.getAccount(), connectedUser.getName());
        model.addAttribute("connectedUser", connectedUser.getName());
        return "redirect:/bidList/list";
    }


    /**
     * This method is displaying bidList/update view with a bid retrieved by its id through {@link BidListService#findById(Integer)}
     * @param id which is the path variable to retrieve bid by id     
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return bidList/update view
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        BidList bidList = bidListService.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid bid id :"+id));
        model.addAttribute("bidList", bidList);
        model.addAttribute("connectedUser", connectedUser.getName());
        return "bidList/update";
    }

    /**
     * This method is used to save a bid which has been updated abd retrieved by its id
     * @param id which is the path variable to retrieve bid by id
     * @param bidList which is the {@link BidList} object updated by user in the form
     * @param result which is a {@link BindingResult} object which has errors if the object validation is not correct
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the bidList/add view if there is an error or the bidList/list if the update is successful
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model, Principal connectedUser) {
        if(result.hasErrors()) {
            StringBuilder errors = new StringBuilder(" : \n");
            result.getAllErrors().forEach(objectError -> errors.append(objectError.getDefaultMessage()).append("\n"));
            logger.error("The following errors occurred when trying to save a bid {}", errors);
            return "bidList/update";
        }
        bidList.setBidListId(id);
        BidList bidUpdated = bidListService.save(bidList);
        logger.info("Bid was saved successfully with current id {} for user {}", bidUpdated.getBidListId(), connectedUser.getName());

        return "redirect:/bidList/list";
    }

    /**
     * This method is deleting a bid by its id by using the service {@link BidListService#delete(Integer)}
     * @param id which is the path variable to retrieve bid by id     
     * @param model  which a {@link Model} object to send infos to the view which will be used by Thymeleaf
     * @param connectedUser which is the user connected retrieved through {@link Principal} object
     * @return the bid/list view after calling /bidList/list endpoint
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model, Principal connectedUser) {
        BidList bid = bidListService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        bidListService.delete(id);
        model.addAttribute("trades", bidListService.findAll());
        logger.info("Bid was deleted successfully for id {} and user {}", id, connectedUser.getName());
        return "redirect:/bidList/list";
    }
}
