/**
 * 
 */
package register;

import java.util.LinkedList;

/**
 * @author Lauro Moraes
 */
public class RegisterTT {
	/**
     * item ID of an element of Table T
     */
	protected Integer itemID;
	 /**
     * List of row IDs where item ID occurs on Table T
     */
	protected LinkedList<Integer> rowSet;
	/**
     * List of row IDs that were excluded from row set
     */
	protected LinkedList<Integer> excludedRowSet;
	/**
     * List of row IDs that was assumed that must be on row set
     */
	protected LinkedList<Integer> skippedRowSet;

	/**
     * Constructor of a RegisterTT by means of a integer that map a item id 
     * @param itemID id of an item in a row of the table T
     */
	public RegisterTT(Integer itemID) {
		this.itemID = itemID;
		this.rowSet = new LinkedList<Integer>();
		this.excludedRowSet = new LinkedList<Integer>();
		this.skippedRowSet = new LinkedList<Integer>();
	}
	/**
     * Adds a row identifier in the list of the set of excluded rows
     * @param rID a row identifier
     */
	public void addExcludedRowId(Integer rID) {
		this.excludedRowSet.add(rID);
	}
	/**
     * It set a new row set given as parameter to the register
     * @param nRowSet list of row IDs that represent the new row set
     */
	public void setNewRowSet(LinkedList<Integer> nRowSet) {
		this.rowSet = nRowSet;
	}
	
	public void setNewSkippedRowSet(LinkedList<Integer> nSkippedRowSet) {
		this.skippedRowSet = nSkippedRowSet;
	}
	
	public void addRowId(Integer rID) {
		this.rowSet.add(rID);
	}
	
	public void addSkippedRowId(Integer rID) {
		this.skippedRowSet.add(rID);
	}
	
	
	
	public Integer getItemID() {
		return this.itemID;
	}
	
	public Integer getMaxRowId() {
		int max = 0;
		try {
			max = this.rowSet.getLast();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return max;
	}
	
	public Integer getRowSetSize() {
		return this.rowSet.size();
	}
	
	public LinkedList<Integer> getRowSet() {
		return this.rowSet;
	}
	
	public Integer getLastFromRowSet() {
		Integer rID = 0;
		if(this.rowSet.size() > 0) {
			rID = this.rowSet.getLast(); 
		}
		return rID;
	}
	
	public LinkedList<Integer> getSkippedRowSet() {
		return this.skippedRowSet;
	}
	
	public Integer getSkippedRowSetSize() {
		return this.skippedRowSet.size();
	}
	
	public LinkedList<Integer> getExcludedRowSet() {
		return this.excludedRowSet;
	}
	
	public Integer getExcludedRowSetSize() {
		return this.excludedRowSet.size();
	}

	@Override
	public String toString() {
		return "RegisterTT [itemID=" + itemID + ", rowSet=" + rowSet
				+ ", excludedRowSet=" + excludedRowSet + ", skippedRowSet="
				+ skippedRowSet + "]";
	}
	
	

}
