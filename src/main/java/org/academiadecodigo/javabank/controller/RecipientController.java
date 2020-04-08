package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.converters.RecipientDtoToRecipient;
import org.academiadecodigo.javabank.converters.RecipientToRecipientDto;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for rendering {@link Recipient} related views
 *
 * @see RecipientService
 */
@Controller
@RequestMapping("/customer")
public class RecipientController {

    private RecipientService recipientService;
    private CustomerService customerService;

    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtoToRecipient;
    private CustomerToCustomerDto customerToCustomerDto;

    /**
     * Sets the recipient service
     *
     * @param recipientService the recipient service to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    /**
     * Sets the customer service
     *
     * @param customerService the customer service to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Sets the converter for converting between recipient model objects and recipient dto objects
     *
     * @param recipientToRecipientDto the recipient to recipient dto converter to set
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    /**
     * Sets the converter for converting between recipient dto objects and recipient model objects
     *
     * @param recipientDtoToRecipient the recipient dto to recipient converter to set
     */
    @Autowired
    public void setRecipientDtoToRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtoToRecipient = recipientDtoToRecipient;
    }

    /**
     * Sets the converter for converting between customer model objects and customer dto objects
     *
     * @param customerToCustomerDto the customer to customer dto converter to set
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Adds a recipient
     *
     * @param cid   the customer id
     * @param model th model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/add")
    public String addRecipient(@PathVariable Integer cid, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));
        model.addAttribute("recipient", new RecipientDto());
        return "recipient/add-update";
    }

    /**
     * Edits a recipient from a customer
     *
     * @param cid   the customer id
     * @param id    the recipient id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}/edit")
    public String editRecipient(@PathVariable Integer cid, @PathVariable Integer id, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));
        model.addAttribute("recipient", recipientToRecipientDto.convert(recipientService.get(id)));
        return "recipient/add-update";
    }

    /**
     * Saves the recipient form submission and renders a view with the customer details
     *
     * @param cid                the customer id
     * @param recipientDto      the recipient form
     * @param redirectAttributes the redirect attributes model
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=save")
    public String saveRecipient(@PathVariable Integer cid, @ModelAttribute("recipient") RecipientDto recipientDto, RedirectAttributes redirectAttributes) {
        customerService.addRecipient(cid, recipientDtoToRecipient.convert(recipientDto));
        redirectAttributes.addFlashAttribute("lastAction", "Saved " + recipientDto.getName());
        return "redirect:/customer/" + cid;
    }

    /**
     * Cancels the recipient form submission and renders a view with the customer details
     *
     * @param cid the customer id
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=cancel")
    public String cancelSaveRecipient(@PathVariable Integer cid) {
        // we could use an anchor tag in the view for this, but we might want to do something clever in the future here
        return "redirect:/customer/" + cid;
    }

    /**
     * Deletes the recipient and renders a view with the customer details
     *
     * @param cid                the customer id
     * @param id                 the recipient id
     * @param redirectAttributes the redirect attributes model
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}/delete")
    public String deleteRecipient(@PathVariable Integer cid, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Recipient recipient = recipientService.get(id);
        customerService.removeRecipient(cid, id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted " + recipient.getName());
        return "redirect:/customer/" + cid;
    }
}
