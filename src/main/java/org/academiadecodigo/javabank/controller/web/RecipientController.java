package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.converters.*;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.RecipientService;
import org.academiadecodigo.javabank.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller responsible for rendering {@link Recipient} related views
 */
@Controller
@RequestMapping("/customer")
public class RecipientController {

    private RecipientService recipientService;
    private CustomerService customerService;
    private TransferService transferService;

    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtoToRecipient;
    private CustomerToCustomerDto customerToCustomerDto;
    private TransferDtoToTransfer transferDtoToTransfer;
    private AccountToAccountDto accountToAccountDto;

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
     * Sets the transfer service
     *
     * @param transferService the transfer service to set
     */
    @Autowired
    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Sets the converter for converting between recipient model objects and recipient DTO
     *
     * @param recipientToRecipientDto the recipient to recipient DTO converter
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    /**
     * Sets the converter for converting between recipient DTO and recipient model objects
     *
     * @param recipientDtoToRecipient the recipient DTO to recipient converter
     */
    @Autowired
    public void setRecipientDtoToRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtoToRecipient = recipientDtoToRecipient;
    }

    /**
     * Sets the converter for converting between customer model objects and DTO
     *
     * @param customerToCustomerDto the customer to customer DTO converter
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Sets the converter for converting between transfer DTO and transfer domain objects
     *
     * @param transferDtoToTransfer the transfer DTO to transfer converter
     */
    @Autowired
    public void setTransferDtoToTransfer(TransferDtoToTransfer transferDtoToTransfer) {
        this.transferDtoToTransfer = transferDtoToTransfer;
    }

    /**
     * Sets the converter for converting between account model object and account DTO
     *
     * @param accountToAccountDto the account model object to account DTO converter to set
     */
    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }

    /**
     * Renders a view with a list of recipients
     *
     * @param cid   the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/{cid}/recipient", "/{cid}/recipient/list"})
    public String listRecipients(@PathVariable Integer cid, Model model) {

        try {

            List<Recipient> recipients = customerService.listRecipients(cid);
            Customer customer = customerService.get(cid);

            model.addAttribute("recipients", recipientToRecipientDto.convert(recipients));
            model.addAttribute("customer", customerToCustomerDto.convert(customer));
            model.addAttribute("accounts", accountToAccountDto.convert(customer.getAccounts()));

            return "recipient/list";

        } catch (CustomerNotFoundException ex) {
            return "redirect:/";
        }
    }

    /**
     * Adds a recipient
     *
     * @param cid   the customer id
     * @param model the model object
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
     * Saves the recipient DTO and renders a view with customer details
     *
     * @param model              the model object
     * @param cid                the customer id
     * @param recipientDto       the recipient DTO
     * @param bindingResult      the binding result object
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=save")
    public String saveRecipient(Model model, @PathVariable Integer cid, @Valid @ModelAttribute("recipient") RecipientDto recipientDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));

        if (bindingResult.hasErrors()) {
            return "recipient/add-update";
        }

        try {

            Recipient recipient = recipientDtoToRecipient.convert(recipientDto);
            customerService.addRecipient(cid, recipient);

            redirectAttributes.addFlashAttribute("lastAction", "Saved " + recipientDto.getName());
            return "redirect:/customer/" + cid + "/recipient";

        } catch (AccountNotFoundException ex) {

            bindingResult.rejectValue("accountNumber", "invalid.account", "invalid account number");
            return "recipient/add-update";

        } catch (CustomerNotFoundException ex) {

            return "redirect:/customer/" + cid;
        }
    }

    /**
     * Cancels the recipient DTO submission and renders a view with customer details
     *
     * @param cid the customer id
     * @return the vuew to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=cancel")
    public String cancelSaveRecipient(@PathVariable Integer cid) {
        //we could use an anchor tag in the view for this, but we might want to do something clever in the future here
        return "redirect:/customer/" + cid;
    }

    /**
     * Deletes a recipient and a renders a view with the customer details
     *
     * @param cid                the customer id
     * @param id                 the recipient id
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}/delete")
    public String deleteRecipient(@PathVariable Integer cid, @PathVariable Integer id, RedirectAttributes redirectAttributes) throws Exception {
        Recipient recipient = recipientService.get(id);
        customerService.removeRecipient(cid, id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted " + recipient.getName());
        return "redirect:/customer/" + cid + "/recipient";
    }

    /**
     * Renders a view with a transfer DTO
     *
     * @param cid   the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/transfer")
    public String transfer(@PathVariable Integer cid, Model model) {

        try {

            List<Recipient> recipients = customerService.listRecipients(cid);
            Customer customer = customerService.get(cid);

            model.addAttribute("recipients", recipientToRecipientDto.convert(recipients));
            model.addAttribute("customer", customerToCustomerDto.convert(customer));
            model.addAttribute("accounts", accountToAccountDto.convert(customer.getAccounts()));
            model.addAttribute("transfer", new TransferDto());
            return "recipient/transfer";

        } catch (CustomerNotFoundException ex) {
            return "redirect:/customer/" + cid;
        }
    }

    /**
     * Makes a transfer between accounts and renders the default customer view
     *
     * @param model              the model object
     * @param cid                the customer id
     * @param transferDto        the transfer DTO
     * @param bindingResult      the binding result object
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/transfer"})
    public String doTransfer(Model model, @PathVariable Integer cid, @Valid @ModelAttribute("transfer") TransferDto transferDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            Customer customer = customerService.get(cid);

            model.addAttribute("customer", customerToCustomerDto.convert(customer));
            model.addAttribute("accounts", accountToAccountDto.convert(customer.getAccounts()));
            model.addAttribute("recipients", recipientToRecipientDto.convert(customerService.listRecipients(cid)));
            redirectAttributes.addFlashAttribute("failure", "Transfer failed missing information");
            return "recipient/transfer";
        }

        try {
            transferService.transfer(transferDtoToTransfer.convert(transferDto), cid);

            redirectAttributes.addFlashAttribute("lastAction", "Transfered " + transferDto.getAmount() + " to account #" + transferDto.getDstId());
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform transaction: value above the allowed amount");
            return "redirect:/customer/" + cid + "/recipient/transfer";
        }
    }
}
