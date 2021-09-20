package com.java.BankDemo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAO {

	Connection connection;
	PreparedStatement pst;
	
	public String withdrawAccount(int accountNo, int withdrawAmount) throws ClassNotFoundException, SQLException
	{
		Bank bank = searchAccount(accountNo);
		if (bank!=null)
		{
			connection = ConnectionHelper.getConnection();
			int balance = bank.getAmount();
			
			if ( balance - withdrawAmount > 1000)
				{
				//int newbalance = balance - withdrawAmount;
				String cmd = "Update bank Set Amount = Amount-? Where AccountNo=?";
				pst = connection.prepareStatement(cmd);
				pst.setInt(1,withdrawAmount);
				pst.setInt(2, accountNo);
				pst.executeUpdate();
				cmd = "insert into trans(AccountNo,TransAmount,TransType)"
						+"Values(?,?,?)";
				pst= connection.prepareStatement(cmd);
				pst.setInt(1, accountNo);
				pst.setInt(2, withdrawAmount);
				pst.setString(3, "C");
				pst.executeUpdate();
				return "Withdrawl complete";
				}
			else {
				return "Insufficient funds";
			}
		}
		else
		{
			return "Account not found";
		}
	}
	
	public String depositAccount(int accountNo, int depositAmount) throws ClassNotFoundException, SQLException {
		Bank bank = searchAccount(accountNo);
		if (bank != null) {
			connection =ConnectionHelper.getConnection();
			String cmd = "Update bank Set Amount = Amount+? Where AccountNo=?";
			pst = connection.prepareStatement(cmd);
			pst.setInt(1, depositAmount);
			pst.setInt(2, accountNo);
			pst.executeUpdate();
			cmd = "Insert into Trans(AccountNo,TransAmount,TransType) "
					+ " VALUES(?,?,?)";
			pst = connection.prepareStatement(cmd);
			pst.setInt(1, accountNo);
			pst.setInt(2, depositAmount);
			pst.setString(3, "C");
			pst.executeUpdate();
			return "Amount Credited...";
		}
		return "Account No Not Found...";
	}
	public String closeAccount(int accountNo) throws ClassNotFoundException, SQLException {
		Bank bank = searchAccount(accountNo);
		if (bank!=null) {
			connection =ConnectionHelper.getConnection();
			String cmd = "update bank set status='inactive' where accountNo=?";
			pst = connection.prepareStatement(cmd);
			pst.setInt(1, accountNo);
			pst.executeUpdate();
			return "Account Closed...";
		}
		return "AccountNo Not Found...";
	}
	
	public Bank searchAccount(int accountNo) throws ClassNotFoundException, SQLException {
		connection =ConnectionHelper.getConnection();
		String cmd = "select * from Bank where AccountNo=?";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, accountNo);
		ResultSet rs = pst.executeQuery();
		Bank bank = null;
		if (rs.next()) {
			bank = new Bank();
			bank.setAccountNo(rs.getInt("accountNo"));
			bank.setFirstName(rs.getString("FirstName"));
			bank.setLastName(rs.getString("LastName"));
			bank.setCity(rs.getString("city"));
			bank.setState(rs.getString("state"));
			bank.setAmount(rs.getInt("amount"));
			bank.setCheqFacil(rs.getString("cheqFacil"));
			bank.setAccountType(rs.getString("accountType"));
		}
		return bank;
	}
	public String createAccount(Bank bank) throws ClassNotFoundException, SQLException {
		connection =ConnectionHelper.getConnection();
		int accno = generateAccountNo();
		bank.setAccountNo(accno);
		String cmd = "Insert into Bank(AccountNo,FirstName,LastName,City,State,"
				+ "Amount,CheqFacil,AccountType) values(?,?,?,?,?,?,?,?)";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, bank.getAccountNo());
		pst.setString(2, bank.getFirstName());
		pst.setString(3, bank.getLastName());
		pst.setString(4, bank.getCity());
		pst.setString(5, bank.getState());
		pst.setInt(6, bank.getAmount());
		pst.setString(7, bank.getCheqFacil());
		pst.setString(8, bank.getAccountType());
		pst.executeUpdate();
		return "Account Created Successfully...";
	}
	public int generateAccountNo() throws ClassNotFoundException, SQLException {
		connection = ConnectionHelper.getConnection();
		String cmd = "select case when max(accountNo) IS NULL THEN 1 ELSE "
				+ " max(accountNo)+1 END accno from Bank";
		pst = connection.prepareStatement(cmd);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int accountNo = rs.getInt("accno");
		return accountNo;
	}
	public String updateAccount(Bank bank) throws ClassNotFoundException, SQLException {
		connection =ConnectionHelper.getConnection();
		int accno = generateAccountNo();
		bank.setAccountNo(accno);
		String cmd = "update Bank set AccountNo=?,FirstName=?,LastName=?,City=?,"
				+ "State=?,"
				+ "Amount=>,CheqFacil=?,AccountType=?";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, bank.getAccountNo());
		pst.setString(2, bank.getFirstName());
		pst.setString(3, bank.getLastName());
		pst.setString(4, bank.getCity());
		pst.setString(5, bank.getState());
		pst.setInt(6, bank.getAmount());
		pst.setString(7, bank.getCheqFacil());
		pst.setString(8, bank.getAccountType());
		pst.executeUpdate();
		return "Account Updated Successfully...";
	}
	
	
}
