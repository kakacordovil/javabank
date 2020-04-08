package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for rendering {@link Customer} related views
 */
@RequestMapping("/customer")
@Controller
public class CustomerController {

    private CustomerService customerService;

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
     * Renders a view with a list of customers
     *
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.list());
        return "customer/list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/show/{id}")
    public String showCustomer(Model model,
                               @PathVariable Integer id){

        Customer customer = customerService.get(id);
        model.addAttribute("customer", customer);
        model.addAttribute("accounts", customer.getAccounts());
        return "show-info";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/delete/{id}")
    public String delete(@PathVariable Integer id) {

        customerService.deleteCustomer(id);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/create")
    public String create(Model model, @ModelAttribute("customer")  Customer customer) {

//        Customer newCustomer = customerService.create(customer);
        Customer newCustomer = new Customer();
        model.addAttribute("customer", newCustomer);

        return "create-customer";
    }

    @RequestMapping(method = RequestMethod.POST, path = {"/add"})
    public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes) {

        Customer savedCustomer = customerService.save(customer);
        redirectAttributes.addFlashAttribute("lastAction", "Added customer successfully!");
        return "redirect:/customer/list";

    }

    @RequestMapping(method = RequestMethod.GET, path = "/update/{id}")
    public String update(Model model, @PathVariable Integer id) {

        Customer customer = customerService.get(id);
//        customer.setFirstName(customer.getFirstName());
//        customer.setLastName(customer.getLastName());
//        customer.setEmail(customer.getEmail());
//        customer.setPhone(customer.getPhone());
//        customer.setId(id);
       // customerService.save(customer);
        System.out.println("-In Customer Controller Update Method: "+customer);

        model.addAttribute("customer",customer);

        return "update-customer";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    public String saveUpdate(@ModelAttribute("customer") Customer customer) {
    System.out.println("-In Customer Controller: "+customer);
        customerService.update(customer);
        return "redirect:/customer/list";
    }


}
