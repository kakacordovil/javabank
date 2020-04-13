package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.Recipient;

import javax.validation.constraints.*;

/**
 * The {@link Recipient} data transfer object
 */
public class RecipientDto {

    private Integer id;

    @NotNull(message = "Account number is mandatory")
    private Integer accountNumber;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 64)
    private String name;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number contains invalid characters")
    @Size(min = 9, max = 16)
    private String phone;

    private String description;

    /**
     * Gets the recipient DTO id
     *
     * @return the recipient DTO
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the recipient DTO id
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the recipient DTO account number
     *
     * @return the recipient DTO account number
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the recipient DTO account number
     *
     * @param accountNumber the account number to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the recipient DTO name
     *
     * @return the recipient DTO name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the recipient DTO name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the recipient DTO name
     *
     * @return the recipient DTO email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the recipient DTO email
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the recipient DTO phone
     *
     * @return the recipient DTO phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the recipient DTO phone
     *
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the recipient DTO description
     *
     * @return the recipient DTO description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the recipient DTO description
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "RecipientForm{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
