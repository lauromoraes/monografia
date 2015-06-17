/**
 * 
 */
package tdclose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import register.RegisterTT;
import table.TableT;
import table.TableTT;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * This is an implementation of the TD-Close algorithm as described by :
 * 
 * Liu, H., Wang, X., He, J., Han, J., Xin, D. and Shao, Z. (2009). Top-down
 * mining of frequent closed patterns from very high dimensional data. Information
 * Sciences: Elsevier Science, 179(1), pages 899-924 .
 * <br/><br/>
 * <br/><br/>
 * 
 * @author Lauro Moraes
 *
 */
public class TDClose {
	
	protected TableT tableT;
	protected TableTT tableTT;
	protected TreeSet<Integer> FCI;
	protected Integer MinSup;
	protected Integer threshold;
	
	public TDClose() {
		this.FCI = new TreeSet<Integer>();
		this.MinSup = 2;
	}
	
	public void topDownMine(TableTT tableTT) {
		
//		tableTT.printTableTT(false);
		
		/* Pruning 1 */
		Integer excludedSize = tableTT.getExludedSize();
		Integer skippedSize = tableTT.getSkippedSize();
		if(excludedSize >= this.threshold || skippedSize >= this.tableT.getTableSize()) {
			return;
		}
		/* Pruning 2 */
		if(tableTT.getTableSize() == 1) {
			RegisterTT reg = tableTT.getTableTT().entrySet().iterator().next().getValue();

			if( this.closennesChecking(reg) ) {
				
				HashSet<Integer> itens = new HashSet<Integer>();
				for(String item : this.tableT.getNamedValue( reg.getItemID() ).split(",") ){
					itens.add(this.tableT.getIndexedValue(item));
				}
				this.FCI.add(reg.getItemID());
			}
			return;
		}

		/* Pruning 3 */
		TableTT tt = null;
		/* TTxUy */
		tt = tableTT.derive(1);
//		tt.printTableTT(false);
		if(tt.getTableSize() > 0) {
			if(tt.getTableSize() == 1) {
				RegisterTT reg = tt.getTableTT().entrySet().iterator().next().getValue();
				if( this.closennesChecking(reg) ) {
					HashSet<Integer> itens = new HashSet<Integer>();
					for(String item : this.tableT.getNamedValue( reg.getItemID() ).split(",") ){
						itens.add(this.tableT.getIndexedValue(item));
					}
					this.FCI.add(reg.getItemID());
				}
			} else {
				tt.setClosedItemSets();
				HashSet<Integer> fci = tt.getFCI();
				this.FCI.addAll(fci);
				this.topDownMine(tt);
			}
		}
		/* TTx' */
		tt = tableTT.derive(0);
//		tt.printTableTT(false);
		if(tt.getTableSize() > 0) {
			if(tt.getTableSize() == 1) {
				RegisterTT reg = tt.getTableTT().entrySet().iterator().next().getValue();
				if( this.closennesChecking(reg) ) {
					HashSet<Integer> itens = new HashSet<Integer>();
					for(String item : this.tableT.getNamedValue( reg.getItemID() ).split(",") ){
						itens.add(this.tableT.getIndexedValue(item));
					}
					this.FCI.add(reg.getItemID());
				}
			} else {
				this.topDownMine(tt);
			}
		}
		
//		/* Pruning 3 */
//		TableTT[] tables = tableTT.derive();
//		TableTT TTx_ = tables[0];
//		TableTT TTxUy = tables[1];
//		
//		if(TTxUy.getTableSize() > 0) {
//			
//			TTxUy.setClosedItemSets();
//			HashSet<Integer> fci = TTxUy.getFCI();
//			this.FCI.addAll(fci);
//			
//			this.topDownMine(TTxUy);
//		}
//		
//		if(TTx_.getTableSize() > 0) {
//			this.topDownMine(TTx_);
//		}
	}
	public Boolean closennesChecking(RegisterTT reg) {
		if(reg.getExcludedRowSetSize() > 0) {
			return false;
		}
		
//		int size = reg.getRowSetSize();
//		int last = reg.getLastFromRowSet();
		
		if(reg.getRowSetSize() == reg.getLastFromRowSet()) {
			return true;
		}
//		return false;
		
		HashSet<Integer> itens = new HashSet<Integer>();
		for(String item : this.tableT.getNamedValue( reg.getItemID() ).split(",") ){
			itens.add(this.tableT.getIndexedValue(item));
		}
		
		Set<Integer> inter = null;
		ArrayList<Integer> line = null;
		if(reg.getRowSetSize() > 0) {
			line = this.tableT.getLine( reg.getRowSet().getFirst() );
			inter = new HashSet<Integer>( line );
			for(int i=1; i<reg.getRowSetSize(); i++) {
				line = this.tableT.getLine( reg.getRowSet().get(i) );
				inter = Sets.intersection(inter, new HashSet<Integer>( line ));
				if(inter.size() < itens.size() || inter.size() == 0) {
					return false;
				}
			}
			
			for(int i=0; i<reg.getSkippedRowSetSize(); i++) {
				line = this.tableT.getLine( reg.getSkippedRowSet().get(i) );
				inter = Sets.intersection(inter, new HashSet<Integer>( line ));
				if(inter.size() < itens.size() || inter.size() == 0) {
					return false;
				}
			}
			
			if(itens.equals(inter)) {
				return true;
			} else {
				return false;
			}
			
		} else if(reg.getSkippedRowSetSize() > 0) {
			line = this.tableT.getLine( reg.getSkippedRowSet().getFirst() );
			inter = new HashSet<Integer>( line );
			
			for(int i=1; i<reg.getSkippedRowSetSize(); i++) {
				line = this.tableT.getLine( reg.getSkippedRowSet().get(i) );
				inter = Sets.intersection(inter, new HashSet<Integer>( line ));
				if(inter.size() < itens.size() || inter.size() == 0) {
					return false;
				}
			}
			
			if(itens.equals(inter)) {
				return true;
			} else {
				return false;
			}
			
		} else {
			return false;
		}
		
//		LinkedList<Integer> rows = new LinkedList<Integer>();
//		for(Integer row : reg.getRowSet()) {
//			rows.add(row);
//		}
//		for(Integer row : reg.getSkippedRowSet()) {
//			rows.add(row);
//		}
//		Set<Integer> inter = null;
//		
//		if(rows.size() > 1) {
//			ArrayList<Integer> line = this.tableT.getLine( rows.get(0) );
//			inter = new HashSet<Integer>( line );
//			for(int i=1; i<rows.size(); i++) {
//				line = this.tableT.getLine( rows.get(i) );
//				inter = Sets.intersection(inter, new HashSet<Integer>( line ));
//				if(inter.size() == 0 || inter.size() < itens.size()) {
//					return false;
//				}
//			}
//			if(itens.equals(inter)) {
//				return true;
//			} else {
//				return false;
//			}
//		} else if(rows.size() == 1) {
//			ArrayList<Integer> line = this.tableT.getLine( rows.get(0) );
//			if(itens.equals( line )) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
	}
	
	public void setMinSup(Integer ms) {
		this.MinSup = ms;
	}
	
	public void setThreshold() {
		if(this.tableT != null) {
			if(this.MinSup != null) {
				if(this.MinSup <= this.tableT.getTableSize()) {
					this.threshold = this.tableT.getTableSize() - this.MinSup;
				} else {
					System.err.println("<<< ERROR >>>: MinSup has to be lower than the size of TableT.");
				}
			} else {
				System.err.println("<<< NULL MinSup >>>: MinSup has to be setted first.");
			}
		} else {
			System.err.println("<<< NULL TABLE_T >>>: TableT has to be setted first.");
		}
	}
	
	public void setupTableT(String filePath) {
		this.tableT = new TableT();
		this.tableT.readTableFromFile(filePath);
		this.tableT.mapValues();
//		this.tableT.mapValuesInteger();
	}
	
	public void setupTableTT() {
		tableTT = new TableTT(this.tableT, this.MinSup, new LinkedList<Integer>(), new LinkedList<Integer>());
		tableTT.transposeTable();
		tableTT.cutByMinSup();
	}
	
	public TableTT getTableTT() {
		return this.tableTT;
	}
	
	public void printTableT() {
		if(this.tableT != null) {
			this.tableT.printOriginalTable();
		} else {
			System.err.println("<<< NULL TABLE_T >>>: TableT has to be setted first.");
		}
	}
	
	public void printTableTT() {
		if(this.tableTT != null) {
			this.tableTT.printTableTT(false);;
		} else {
			System.err.println("<<< NULL TABLE_TT >>>: TableTT has to be setted first.");
		}
	}
	
	public void printFCI() {
		System.out.println("\n\n========== FREQUENT CLOSED ITEMSET ==========");
		TreeSet<String> set = new TreeSet<String>();
		for(Integer itemID : this.FCI) {
			String name = this.tableT.getNamedValue(itemID);
			String[] itensNames = name.split(",");
			Arrays.sort(itensNames);
			String s = Joiner.on(",").join(itensNames);
			set.add(s);
		}
		long cnt = 0;
		for(String s : set) {
			System.out.println((++cnt) +" - " + s);
		}
	}
}
