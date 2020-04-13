package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.domain.Transfer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.academiadecodigo.javabank.command.AccountDto.moneyRegex;

/**
 * The {@link Transfer} data transfer object
 */
public class TransferDto {

    @NotNull(message = "SrcId is mandatory")
    private Integer srcId;

    @NotNull(message = "DstId is mandatory")
    private Integer dstId;

    @NotBlank(message = "InitialAmount is mandatory")
    @Pattern(regexp = moneyRegex, message = "Amount is not valid")
    @NotNull(message = "Amount is mandatory")
    private String amount;

    /**
     * Gets the source id of the transfer DTO
     *
     * @return the transfer DTO source id
     */
    public Integer getSrcId() {
        return srcId;
    }

    /**
     * Sets the source id of the transfer DTO
     *
     * @param srcId the id to set
     */
    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    /**
     * Gets the destination id of the transfer DTO
     *
     * @return the transfer DTO destination id
     */
    public Integer getDstId() {
        return dstId;
    }

    /**
     * Sets the destination id of the transfer DTO
     *
     * @param dstId the id to set
     */
    public void setDstId(Integer dstId) {
        this.dstId = dstId;
    }

    /**
     * Gets the amount of the transfer DTO
     *
     * @return the transfer DTO amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transfer DTO
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
        return "\nTransferForm{" +
                "\nsrcId=" + srcId +
                ", \ndstId=" + dstId +
                ", \namount='" + amount + '\'' +
                '}';
    }
}
