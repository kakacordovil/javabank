package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.command.AccountTransactionDto;
import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.converters.AccountDtoToAccount;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.converters.TransferDtoToTransfer;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller responsible for rendering {@link Account} related views
 */
@Controller
@RequestMapping("/customer")
public class AccountController {

    private CustomerService customerService;
    private TransferService transferService;
    private AccountService accountService;

    private AccountDtoToAccount accountDtoToAccount;
    private TransferDtoToTransfer transferDtoToTransfer;
    private CustomerToCustomerDto customerToCustomerDto;

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
     * Sets the account service
     *
     * @param accountService the account service to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Sets the converter for converting between account DTO and account model objects
     *
     * @param accountDtoToAccount the account DTO to account converter to set
     */
    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }

    /**
     * Sets the converter for converting between transfer DTO objects and transfer domain objects
     *
     * @param transferDtoToTransfer the transfer form to transfer converter to set
     */
    @Autowired
    public void setTransferDtoToTransfer(TransferDtoToTransfer transferDtoToTransfer) {
        this.transferDtoToTransfer = transferDtoToTransfer;
    }

    /**
     * Sets the converter for converting between customer model objects and customer DTO
     *
     * @param customerToCustomerDto the customer to customer DTO converter to set
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Adds an account
     *
     * @param cid                the customer id
     * @param accountDto         the account data transfer object
     * @param bindingResult      the binding result object
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/account"})
    public String addAccount(@PathVariable Integer cid, @Valid @ModelAttribute("account") AccountDto accountDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            return "redirect:/customer/" + cid;
        }

        try {
            Account account = accountDtoToAccount.convert(accountDto);
            customerService.addAccount(cid, account);
            redirectAttributes.addFlashAttribute("lastAction", "Created " + account.getAccountType() + " account.");
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Savings account must have a minimum value of 100 at all times");
            return "redirect:/customer/" + cid;
        }
    }

    /**
     * Deposits a given amount to an account
     *
     * @param cid                   the customer id
     * @param accountTransactionDto the account transaction data transfer object
     * @param bindingResult         the binding result object
     * @param redirectAttributes    the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/deposit"})
    public String deposit(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") AccountTransactionDto accountTransactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Deposit failed missing information");
            return "redirect:/customer/" + cid;
        }

        accountService.deposit(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));
        redirectAttributes.addFlashAttribute("lastAction", "Deposited " + accountTransactionDto.getAmount() + " into account # " + accountTransactionDto.getId());
        return "redirect:/customer/" + cid;
    }

    /**
     * Withdraws a given amount from an account
     *
     * @param cid                   the customer id
     * @param accountTransactionDto the account transaction data transfer object
     * @param bindingResult         the binding result object
     * @param redirectAttributes    the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/withdraw"})
    public String withdraw(@PathVariable Integer cid, @Valid @ModelAttribute("transaction") AccountTransactionDto accountTransactionDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            //this message appears when the form is submitted with value blank
            redirectAttributes.addFlashAttribute("failure", "Withdraw failed missing information");
            return "redirect:/customer/" + cid;
        }

        try {
            accountService.withdraw(accountTransactionDto.getId(), cid, Double.parseDouble(accountTransactionDto.getAmount()));
            redirectAttributes.addFlashAttribute("lastAction", "Withdrew " + accountTransactionDto.getAmount() + " from account # " + accountTransactionDto.getId());
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Withdraw failed. " + accountTransactionDto.getAmount() + " is over the current balance for account # " + accountTransactionDto.getId());
            return "redirect:/customer/" + cid;
        }
    }

    /**
     * Closes an account
     *
     * @param cid                the customer id
     * @param aid                the account id
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account/{aid}/close")
    public String closeAccount(@PathVariable Integer cid, @PathVariable Integer aid, RedirectAttributes redirectAttributes) throws Exception {

        try {
            customerService.closeAccount(cid, aid);
            redirectAttributes.addFlashAttribute("lastAction", "Closed account " + aid);
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform closing operation. Account # " + aid + " still has funds");
            return "redirect:/customer/" + cid;
        }
    }

    /**
     * Transfer an amount between accounts
     *
     * @param cid                the customer id
     * @param transferDto        the transfer data transfer object
     * @param bindingResult      the binding result object
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/transfer"})
    public String transferToAccount(@PathVariable Integer cid, @Valid @ModelAttribute("transfer") TransferDto transferDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws Exception {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("failure", "Transfer failed missing information");
            return "redirect:/customer/" + cid;
        }

        try {
            transferService.transfer(transferDtoToTransfer.convert(transferDto), cid);
            redirectAttributes.addFlashAttribute("lastAction", "Account # " + transferDto.getSrcId() + " transfered " + transferDto.getAmount() + " to account #" + transferDto.getDstId());
            return "redirect:/customer/" + cid;

        } catch (TransactionInvalidException ex) {
            redirectAttributes.addFlashAttribute("failure", "Unable to perform transaction: value above the allowed amount");
            return "redirect:/customer/" + cid;
        }
    }
}
