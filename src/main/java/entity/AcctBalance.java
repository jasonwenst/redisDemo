package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACC_BALANCE_577")
public class AcctBalance implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ACCT_ID")
	private long acctId;
	
	@Column(name = "CUST_ID")
	private long custid;
	
	@Column(name = "BILLING_CYCLE_ID")
	private String billCycId;
	
	@Column(name = "FIRST_UNPAY_MON")
	private String firstUnpayMon;
	
	@Column(name = "FIRST_PRINT_MON")
	private String firstPrintMon;
	
	@Column(name = "SWITCH_MON")
	private String switchMon;
	
	@Column(name = "STATE")
	private int state;
	
	@Column(name = "STATE_DATE")
	private Date stateDate;
	
	@Column(name = "BILL_TYPE")
	private BigDecimal billType;

	public long getAcctId() {
		return acctId;
	}

	public void setAcctId(long acctId) {
		this.acctId = acctId;
	}

	public long getCustid() {
		return custid;
	}

	public void setCustid(long custid) {
		this.custid = custid;
	}

	public String getBillCycId() {
		return billCycId;
	}

	public void setBillCycId(String billCycId) {
		this.billCycId = billCycId;
	}

	public String getFirstUnpayMon() {
		return firstUnpayMon;
	}

	public void setFirstUnpayMon(String firstUnpayMon) {
		this.firstUnpayMon = firstUnpayMon;
	}

	public String getFirstPrintMon() {
		return firstPrintMon;
	}

	public void setFirstPrintMon(String firstPrintMon) {
		this.firstPrintMon = firstPrintMon;
	}

	public String getSwitchMon() {
		return switchMon;
	}

	public void setSwitchMon(String switchMon) {
		this.switchMon = switchMon;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	public BigDecimal getBillType() {
		return billType;
	}

	public void setBillType(BigDecimal billType) {
		this.billType = billType;
	}

	@Override
	public String toString() {
		return "AcctBalance [acctId=" + acctId + ", custid=" + custid + ", billCycId=" + billCycId + ", firstUnpayMon="
				+ firstUnpayMon + ", firstPrintMon=" + firstPrintMon + ", switchMon=" + switchMon + ", state=" + state
				+ ", stateDate=" + stateDate + ", billType=" + billType + "]";
	}

	
}
