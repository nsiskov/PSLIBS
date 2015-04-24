package com.arcot.apps.callout.common;

public class CORequest {

	protected int bankId = 0;
	protected int rangeId = 0;
	protected String cardNumber = null;
	protected String cardholderName = null;

	/**
	 * @return Returns the bankId.
	 */
	public int getBankId() {
		return bankId;
	}

	/**
	 * @param bankId The bankId to set.
	 */
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return Returns the cardNumber.
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber The cardNumber to set.
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return Returns the rangeId.
	 */
	public int getRangeId() {
		return rangeId;
	}

	/**
	 * @param rangeId The rangeId to set.
	 */
	public void setRangeId(int rangeId) {
		this.rangeId = rangeId;
	}

	/**
	 * @return Returns the cardholderName.
	 */
	public String getCardholderName() {
		return cardholderName;
	}

	/**
	 * @param cardholderName The cardholderName to set.
	 */
	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

}
