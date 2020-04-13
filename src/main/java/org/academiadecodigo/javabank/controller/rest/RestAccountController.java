package org.academiadecodigo.javabank.controller.rest;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.converters.AccountDtoToAccount;
import org.academiadecodigo.javabank.converters.AccountToAccountDto;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller responsible for {@link Account} related CRUD operations
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class RestAccountController {

    private CustomerService customerService;
    private AccountService accountService;
    private AccountToAccountDto accountToAccountDto;
    private AccountDtoToAccount accountDtoToAccount;

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
     * Sets the account service
     *
     * @param accountService the account service to set
     */
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
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
     * Sets the converter for converting between account DTO and account model objects
     *
     * @param accountDtoToAccount the account DTO to account converter to set
     */
    @Autowired
    public void setAccountDtoToAccount(AccountDtoToAccount accountDtoToAccount) {
        this.accountDtoToAccount = accountDtoToAccount;
    }

    /**
     * Retrieves a representation of the given customer accounts
     *
     * @param cid the customer id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account")
    public ResponseEntity<List<AccountDto>> listCustomerAccounts(@PathVariable Integer cid) {

        Customer customer = customerService.get(cid);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AccountDto> accountDtos = customer.getAccounts().stream().map(account -> accountToAccountDto.convert(account)).collect(Collectors.toList());

        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

    /**
     * Retrieves a representation of the customer account
     *
     * @param cid the customer id
     * @param aid the account id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account/{aid}")
    public ResponseEntity<AccountDto> showCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Account account = accountService.get(aid);

        if (account == null || account.getCustomer() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!account.getCustomer().getId().equals(cid)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountToAccountDto.convert(account), HttpStatus.OK);
    }

    /**
     * Adds an account
     *
     * @param cid                  the customer id
     * @param accountDto           the account DTO
     * @param bindingResult        the binding result object
     * @param uriComponentsBuilder the uri components builder object
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{cid}/account")
    public ResponseEntity<?> addAccount(@PathVariable Integer cid, @Valid @RequestBody AccountDto accountDto, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors() || accountDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            Account account = customerService.addAccount(cid, accountDtoToAccount.convert(accountDto));

            UriComponents uriComponents = uriComponentsBuilder.path("/api/customer/" + cid + "/account/" + account.getId()).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponents.toUri());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Closes an account
     *
     * @param cid the customer id
     * @param aid the accound id
     * @return the response entity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/account/{aid}/close")
    public ResponseEntity<?> closeAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        try {

            customerService.closeAccount(cid, aid);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (TransactionInvalidException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }
}


