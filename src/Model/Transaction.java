package Model;

public class Transaction {
	 private String transactionId;
	    private String userEmail;
	    private String transactionDate;
	    private int insurance;

	    public int getInsurance() {
	        return insurance;
	    }
	    
	    public Transaction(String transactionId, String userEmail, String transactionDate, int insurance) {
	        this.transactionId = transactionId;
	        this.userEmail = userEmail;
	        this.transactionDate = transactionDate;
	        this.insurance = insurance;
	    }

	    public String getTransactionId() {
	        return transactionId;
	    }

	    public void setTransactionId(String transactionId) {
	        this.transactionId = transactionId;
	    }

	    public String getUserEmail() {
	        return userEmail;
	    }

	    public void setUserEmail(String userEmail) {
	        this.userEmail = userEmail;
	    }

	    public String getTransactionDate() {
	        return transactionDate;
	    }

	    public void setTransactionDate(String transactionDate) {
	        this.transactionDate = transactionDate;
	    }
}
