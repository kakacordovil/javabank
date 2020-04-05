package org.academiadecodigo.javabank.persistence.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The recipient model entity
 */
@Entity
@Table(name = "recipient")
public class Recipient extends AbstractModel {

    private String name;
    private String email;
    private String phone;
    private Integer accountNumber;
    private String description;

    @ManyToOne
    private Customer customer;

    /**
     * Gets the name of the recipient
     *
     * @return the recipient name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the recipient
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the recipient
     *
     * @return the recipient email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the recipient
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone of the recipient
     *
     * @return the recipient phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone of the recipient
     *
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the account number
     *
     * @return the account number
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number
     *
     * @param accountNumber the account number to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the recipient description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the recipient description
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the customer
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Recipient{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", accountNumber=" + accountNumber +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
