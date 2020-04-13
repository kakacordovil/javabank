package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.account.Account;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.academiadecodigo.javabank.command.AccountDto.moneyRegex;

/**
 * The {@link Account} transactions data transfer object
 */
public class AccountTransactionDto {

    @NotNull(message = "Id is mandatory")
    private Integer id;

    @NotBlank(message = "InitialAmount is mandatory")
    @Pattern(regexp = moneyRegex, message = "Amount is not valid")
    @NotNull(message = "Amount is mandatory")
    private String amount;

    /**
     * Gets the id of the transaction DTO
     *
     * @return the account DTO id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the transaction DTO
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the amount of the transaction DTO
     *
     * @return the transaction DTO amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction DTO
     *
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "AccountTransactionForm{" +
                "id=" + id +
                ", amount='" + amount + '\'' +
                '}';
    }
}
