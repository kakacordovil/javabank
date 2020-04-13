package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.Customer;

import javax.validation.constraints.*;

/**
 * The {@link Customer} data transfer object
 */
public class CustomerDto {

    private Integer id;

    @NotNull(message = "First name is mandatory")
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 64)
    private String firstName;

    @NotNull(message = "First name is mandatory")
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 64)
    private String lastName;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number contains invalid characters")
    @Size(min = 9, max = 16)
    private String phone;

    /**
     * Gets the id of the customer DTO
     *
     * @return the customer DTO id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the customer DTO
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the first name of the customer DTO
     *
     * @return the customer DTO first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer DTO
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the customer DTO
     *
     * @return the customer DTO last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the customer DTO
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email of the customer DTO
     *
     * @return the customer DTO email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the customer DTO
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone of the customer DTO
     *
     * @return the customer DTO phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone of the customer DTP
     *
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "CustomerForm{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
