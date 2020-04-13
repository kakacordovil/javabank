package org.academiadecodigo.javabank.domain;

import org.academiadecodigo.javabank.persistence.model.account.Account;

/**
 * The transfer domain entity,
 * responsible for transfers between two {@link Account}
 */
public class Transfer {

    private Integer srcId;
    private Integer dstId;
    private Double amount;

    /**
     * Gets the id of the source account
     *
     * @return the source id
     */
    public Integer getSrcId() {
        return srcId;
    }

    /**
     * Sets the id of the source account
     *
     * @param srcId the id to set
     */
    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    /**
     * Gets the id of destination account
     *
     * @return the destination id
     */
    public Integer getDstId() {
        return dstId;
    }

    /**
     * Sets the id of the destination account
     *
     * @param dstId the id to set
     */
    public void setDstId(Integer dstId) {
        this.dstId = dstId;
    }

    /**
     * Gets the amount of the transfer
     *
     * @return the  transfer amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transfer
     *
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Transfer{" +
                "srcId=" + srcId +
                ", dstId=" + dstId +
                ", amount=" + amount +
                '}';
    }
}
