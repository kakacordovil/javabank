package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.Recipient;

/**
 * The {@link Recipient} data transfer object
 */
public class RecipientDto {

    private Integer id;
    private Integer accountNumber;
    private String name;
    private String email;
    private String phone;
    private String description;

    /**
     * Gets the recipient dto id
     *
     * @return the recipient dto id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the recipient dto id
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the recipient dto account number
     *
     * @return the recipient dto account number
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the recipient dto account number
     *
     * @param accountNumber the account number to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the recipient dto name
     *
     * @return the recipient dto name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the recipient dto name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the recipient dto email
     *
     * @return the recipient dto email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the recipient dto email
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the recipient dto phone
     *
     * @return the recipient dto phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the recipient dto email
     *
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the recipient dto description
     *
     * @return the recipient dto description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the recipient dto description
     *
     * @param description the recipient dto description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "RecipientDto{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
