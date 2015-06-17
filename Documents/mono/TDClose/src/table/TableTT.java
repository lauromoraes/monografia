/**
 * 
 */
package table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import register.RegisterTT;


/**
 * @author Lauro Moraes
 *
 */
public class TableTT {
	
	protected TableT tableT;
	protected HashMap<Integer, RegisterTT> tableTT;
	protected Integer cMinSup;
	protected Integer Y;
	protected LinkedList<Integer> skippedRowSetTT;
	protected LinkedList<Integer> excludedRowSetTT;
	protected HashSet<Integer> fci;
	
	/* Construtor */
	public TableTT(TableT tableT, Integer cMinSup, LinkedList<Integer> skippedRowSetTT, LinkedList<Integer> excludedRowSetTT) {
		this.cMinSup = cMinSup;								// Configura o valor de cMinSup
		this.tableT = tableT;								// Aponta para a tabela T
		this.tableTT = new HashMap<Integer, RegisterTT>();	// Inicia o indice invertido (tabela TT)
		this.skippedRowSetTT = skippedRowSetTT;
		this.excludedRowSetTT = excludedRowSetTT;
	}
	
	/* Dada a tabela T, gera uma tabela transposta TT a partir dela */
	public void transposeTable() {
		for(Map.Entry<Integer, ArrayList<Integer>> entry : this.tableT.getTable().entrySet()) {	// Para cada linha da tabela T
			Integer rowId = entry.getKey();													// Obtem o numero da linha 
			ArrayList<Integer> itemset = entry.getValue();									// Obtem o itemset presente na linha
			for(Integer item : itemset) {													// Para cada item do itemset obtido
				if(this.tableTT.containsKey(item)) {										// Se o item ja esta presente na tebela transposta TT
					this.tableTT.get(item).addRowId(rowId);									// Adiciona o numero da linha ao rowset do item
				} else {																	// Se o item nao esta presente na tebela transposta TT
					RegisterTT reg = new RegisterTT(item);									// Cria novo objeto do tipo RegisterTT para o novo item
					reg.addRowId(rowId);													// Adiciona o numero da linha ao rowset do item 
					this.tableTT.put(item, reg);											// Insere novo registro do item na tabela TT
				}
			}
		}
	}
	
	public void cutByMinSup() {
		LinkedList<Integer> toCutOff = new LinkedList<Integer>();				// Lista que ira conter os itemIDs dos registros que serao excluidos
		
		/* Seleciona quais são as linhas que serao excluidas da tabela TT */
		for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {	// Para cada linha da tabela TT
			RegisterTT reg = entry.getValue();									// Obtem o registro referente a linha da tabela TT
			if(reg.getRowSetSize() < this.cMinSup) {							// Verifica se o tamanho da rowset e maior que o suporte minimo
				Integer itemId = entry.getKey();								// Obtem o identificador do registro (itemID)
				toCutOff.add(itemId);											// Adiciona a lista dos itens que serao excluidos
			}
		}
		
		/* Efetiva a exclusao das linhas da tabela TT */
		for(Integer itemId : toCutOff) {										// Para cada elemento da listados itens que serao excluidos
			this.tableTT.remove(itemId);										// Remove da tabela TT o item, a partir de seu itemID
		}
	}
	
	public void setY() {
		Integer y = 0;
		for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {
			Integer maxrid = entry.getValue().getMaxRowId(); 
			if(y < maxrid) {
				y = maxrid;
			}
		}
		this.Y = y;
	}
	
	public Integer getY() { 
		if(this.Y == null) {
			this.setY();
		}
		return this.Y;
	}
	
	public Integer getTableSize() {
		return this.tableTT.size();
	}
	
	public Integer getExludedSize() {
		return this.excludedRowSetTT.size();
	}
	
	public Integer getSkippedSize() {
		return this.skippedRowSetTT.size();
	}
	
	public TableT getTableT() {
		return this.tableT;
	}
	
	public HashMap<Integer, RegisterTT> getTableTT() {
		return this.tableTT;
	}
	
	public void addRegister(Integer itemId, RegisterTT reg) {
		this.tableTT.put(itemId, reg);
	}
	
	public RegisterTT getRegister(Integer itemId) {
		return this.tableTT.get(itemId);
	}
	
	public RegisterTT removeRegister(Integer itemID) {
		return this.tableTT.remove(itemID);
	}
	
	public TableTT[] derive() {
		LinkedList<Integer> newSkippedRowSetTT = new LinkedList<Integer>();
		for(Integer item : this.skippedRowSetTT) {
			newSkippedRowSetTT.add(item);
		}
		newSkippedRowSetTT.add(this.getY());
		
		LinkedList<Integer>  newExcludedRowSetTT = new LinkedList<Integer>();
		for(Integer item : this.excludedRowSetTT) {
			newExcludedRowSetTT.add(item);
		}
		newExcludedRowSetTT.add(this.getY());
		
		TableTT ttx		= new TableTT(this.tableT, cMinSup-1, newSkippedRowSetTT, this.excludedRowSetTT);	// Gera uma tabela TTx'
		TableTT ttxUy	= new TableTT(this.tableT, cMinSup, this.skippedRowSetTT, newExcludedRowSetTT);		// Gera uma tabela TTxUy
		
		LinkedList<Integer> changedA = new LinkedList<Integer>();				// Sera usado para armazenar os itemID que foram alterados para a tabela TTx'
		LinkedList<Integer> changedB = new LinkedList<Integer>();				// Sera usado para armazenar os itemID que foram alterados para a tabela TTxUy

//		System.out.println("YY" + this.getY());
		for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {	// Para cada item da tabela TT
			
			Integer itemId = entry.getKey();
			RegisterTT reg = entry.getValue();
			
			if(reg.getMaxRowId().equals(this.getY())  && (int)reg.getRowSet().size() > this.cMinSup) {	// Se o registro contém o Y e se o tamanho de seu rowset é maior que cMinSup
				
				/* Cria novos registros */
				RegisterTT r1 = new RegisterTT(itemId);							// Novo registro a ser adicionado a tabela TTx'
				RegisterTT r2 = new RegisterTT(itemId);							// Novo registro a ser adicionado a tabela TTxUy
				
				/* Gera o rowset derivado */
				LinkedList<Integer> l = reg.getRowSet();						// Retorna a rowSet do registro antigo
				for(int i=0; i<l.size()-1; i++) {								// Para cada rID do registro antigo, fora o último (Y)
					int aux = l.get(i);											// Obtem o rID do registro "pai"
					r1.addRowId(aux);											// Adiciona o rID no novo registro de TTx'
					r2.addRowId(aux);											// Adiciona o rID no novo registro de TTxUy
				}
				
				/* Gera o excludedRowset derivado */
				LinkedList<Integer> x = reg.getExcludedRowSet();				// Retorna a excludedRowset do registro antigo
				for(int i=0; i<x.size(); i++) {									// Para cada rID da excluded rowset do registro antigo
					int aux = x.get(i);											// Obtem o rID do registro "pai"
					r1.addExcludedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTx'
					r2.addExcludedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTxUy
				}
				r2.addExcludedRowId(this.getY());								// Adiciona o rID de Y na excluded rowset do novo registro de TTxUy
				
				/* Gera o skippedRowset derivado */
				LinkedList<Integer> s = reg.getSkippedRowSet();					// Retorna a skippedRowSet do registro antigo
				for(int i=0; i<s.size(); i++) {									// Para cada rID da skipped rowset do registro antigo
					int aux = s.get(i);											// Obtem o rID do registro "pai"
					r1.addSkippedRowId(aux);									// Adiciona o rID na skipped rowset do novo registro de TTx'
					r2.addSkippedRowId(aux);									// Adiciona o rID na skipped rowset do novo registro de TTxUy
				}
				r1.addSkippedRowId(this.getY());								// Adiciona o rID de Y na skipped rowset do novo registro de TTx'
				
				ttx.addRegister(itemId, r1);									// Adiciona o novo registro a nova tabela TTx' derivada de TTx
				
				ttxUy.addRegister(itemId, r2);									// Adiciona o novo registro a nova tabela TTxUy derivada de TTx
				
				changedA.add(itemId);											// Adiciona o itemID a lista dos que foram alterados para a tabela TTx'
				changedB.add(itemId);											// Adiciona o itemID a lista dos que foram alterados para a tabela TTxUy
				
			} else if(reg.getMaxRowId().equals(this.getY()) && (int)reg.getRowSet().size() <= this.cMinSup) { // Se o registro contém o Y
				
				/* Cria novos registro */
				RegisterTT r1 = new RegisterTT(itemId);							// Novo registro a ser adicionado a tabela TTx'
				
				/* Gera o rowset derivado */
				LinkedList<Integer> l = reg.getRowSet();						// Retorna a rowSet do registro antigo
				for(int i=0; i<l.size()-1; i++) {								// Para cada rID do registro antigo, fora o último (Y)
					int aux = l.get(i);											// Obtem o rID do registro "pai"
					r1.addRowId(aux);											// Adiciona o rID no novo registro de TTx'
				}
				
				/* Gera o excludedRowset derivado */
				LinkedList<Integer> x = reg.getExcludedRowSet();				// Retorna a excludedRowset do registro antigo
				for(int i=0; i<x.size(); i++) {									// Para cada rID da excluded rowset do registro antigo
					int aux = x.get(i);											// Obtem o rID do registro "pai"
//					r1.addSkippedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTx'
					r1.addExcludedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTx'
				}
				
				/* Gera o skippedRowset derivado */
				LinkedList<Integer> s = reg.getSkippedRowSet();					// Retorna a skippedRowSet do registro antigo
				for(int i=0; i<s.size(); i++) {									// Para cada rID da skipped rowset do registro antigo
					int aux = s.get(i);											// Obtem o rID do registro "pai"
					r1.addSkippedRowId(aux);									// Adiciona o rID na skipped rowset do novo registro de TTx'
				}
				r1.addSkippedRowId(this.getY());								// Adiciona o rID de Y na skipped rowset do novo registro de TTx'
				
				ttx.addRegister(itemId, r1);									// Adiciona o novo registro a nova tabela TTx' derivada de TTx
				
				changedA.add(itemId);											// Adiciona o itemID a lista dos que foram alterados para a tabela TTx'
				
//				if(reg.getItemID().equals( 497 )) {
//					System.out.println(this.tableTT.size() + " - " + this.getY() + " - " + reg);
//					ttx.printTableTT(false);
//				}
				
			} else {
				ttxUy.addRegister(itemId, reg);									// Adiciona o registro que nao contem Y a tabela TTxUy
			}
			
		}
		
//		System.out.println("MERGE TTx");
		this.mergeRows(ttx, changedA);
//		System.out.println("MERGE TTxUy");
		this.mergeRows(ttxUy, changedB);
		
//		ttxUy.setClosedItemSets();
		
		TableTT[] v = {ttx, ttxUy};												// Monta um vetor "v" com ambas as tabelas derivadas (TTx' e TTxUy, respectivamente)
		return v;																// Retorna as tabelas derivadas atraves do vetor "v"
	}
	
	public TableTT derive(int type) {
		TableTT tt = null;
		if(type == 0) {
			LinkedList<Integer> newSkippedRowSetTT = new LinkedList<Integer>();
			for(Integer item : this.skippedRowSetTT) {
				newSkippedRowSetTT.add(item);
			}
			newSkippedRowSetTT.add(this.getY());
			tt	= new TableTT(this.tableT, cMinSup-1, newSkippedRowSetTT, this.excludedRowSetTT);	// Gera uma tabela TTx'
		} else {
			LinkedList<Integer>  newExcludedRowSetTT = new LinkedList<Integer>();
			for(Integer item : this.excludedRowSetTT) {
				newExcludedRowSetTT.add(item);
			}
			newExcludedRowSetTT.add(this.getY());
			tt	= new TableTT(this.tableT, cMinSup, this.skippedRowSetTT, newExcludedRowSetTT);		// Gera uma tabela TTxUy
		}
		LinkedList<Integer> changed = new LinkedList<Integer>();					// Sera usado para armazenar os itemID que foram alterados para a tabela

		for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {		// Para cada item da tabela TT
			
			Integer itemId = entry.getKey();
			RegisterTT reg = entry.getValue();
			if(type == 0) {
				if(reg.getMaxRowId().equals(this.getY())) {
					/* Cria novos registro */
					RegisterTT r = new RegisterTT(itemId);							// Novo registro a ser adicionado a tabela TTx'
					
					/* Gera o rowset derivado */
					LinkedList<Integer> l = reg.getRowSet();						// Retorna a rowSet do registro antigo
					for(int i=0; i<l.size()-1; i++) {								// Para cada rID do registro antigo, fora o último (Y)
						int aux = l.get(i);											// Obtem o rID do registro "pai"
						r.addRowId(aux);											// Adiciona o rID no novo registro de TTx'
					}
					
					/* Gera o excludedRowset derivado */
					LinkedList<Integer> x = reg.getExcludedRowSet();				// Retorna a excludedRowset do registro antigo
					for(int i=0; i<x.size(); i++) {									// Para cada rID da excluded rowset do registro antigo
						int aux = x.get(i);											// Obtem o rID do registro "pai"
						r.addExcludedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTx'
					}
					
					/* Gera o skippedRowset derivado */
					LinkedList<Integer> s = reg.getSkippedRowSet();					// Retorna a skippedRowSet do registro antigo
					for(int i=0; i<s.size(); i++) {									// Para cada rID da skipped rowset do registro antigo
						int aux = s.get(i);											// Obtem o rID do registro "pai"
						r.addSkippedRowId(aux);										// Adiciona o rID na skipped rowset do novo registro de TTx'
					}
					r.addSkippedRowId(this.getY());									// Adiciona o rID de Y na skipped rowset do novo registro de TTx'
					
					tt.addRegister(itemId, r);										// Adiciona o novo registro a nova tabela TTx' derivada de TTx
					changed.add(itemId);											// Adiciona o itemID a lista dos que foram alterados para a tabela TTx'
				}
			} else {
				if(reg.getMaxRowId().equals(this.getY())  && (int)reg.getRowSet().size() > this.cMinSup) {	// Se o registro contém o Y e se o tamanho de seu rowset é maior que cMinSup
					/* Cria novos registros */
					RegisterTT r = new RegisterTT(itemId);							// Novo registro a ser adicionado a tabela TTxUy
					
					/* Gera o rowset derivado */
					LinkedList<Integer> l = reg.getRowSet();						// Retorna a rowSet do registro antigo
					for(int i=0; i<l.size()-1; i++) {								// Para cada rID do registro antigo, fora o último (Y)
						int aux = l.get(i);											// Obtem o rID do registro "pai"
						r.addRowId(aux);											// Adiciona o rID no novo registro de TTxUy
					}
					
					/* Gera o excludedRowset derivado */
					LinkedList<Integer> x = reg.getExcludedRowSet();				// Retorna a excludedRowset do registro antigo
					for(int i=0; i<x.size(); i++) {									// Para cada rID da excluded rowset do registro antigo
						int aux = x.get(i);											// Obtem o rID do registro "pai"
						r.addExcludedRowId(aux);									// Adiciona o rID na excluded rowset do novo registro de TTxUy
					}
					r.addExcludedRowId(this.getY());								// Adiciona o rID de Y na excluded rowset do novo registro de TTxUy
					
					/* Gera o skippedRowset derivado */
					LinkedList<Integer> s = reg.getSkippedRowSet();					// Retorna a skippedRowSet do registro antigo
					for(int i=0; i<s.size(); i++) {									// Para cada rID da skipped rowset do registro antigo
						int aux = s.get(i);											// Obtem o rID do registro "pai"
						r.addSkippedRowId(aux);										// Adiciona o rID na skipped rowset do novo registro de TTxUy
					}
					
					tt.addRegister(itemId, r);										// Adiciona o novo registro a nova tabela TTxUy derivada de TTx
					changed.add(itemId);											// Adiciona o itemID a lista dos que foram alterados para a tabela TTxUy
					
				} else if(!reg.getMaxRowId().equals(this.getY())) {
					tt.addRegister(itemId, reg);									// Adiciona o registro que nao contem Y a tabela TTxUy
				}
			}			
		}
		this.mergeRows(tt, changed);
//		ttxUy.setClosedItemSets();
		return tt;																// Retorna as tabelas derivadas atraves do vetor "v"
	}
	
	public void mergeRows(TableTT tt, LinkedList<Integer> changeds) {
		while(changeds.size() > 0) {													// Enquanto houver itemID na lista dos rowsets que foram alterados
			Integer citemID = changeds.pop();											// Obtem um itemID da lista dos rowsets alterados
			
			LinkedList<Integer> toMerge = new LinkedList<Integer>();				// Ira armazenar os elementos que sofrerao merge
			toMerge.add(citemID);													// Adiciona o elemento com rowset modificado
			Boolean hasAny = false;													// Flag de controle para saber se sera necessario o merge
			
			for(Map.Entry<Integer, RegisterTT> entry : tt.getTableTT().entrySet()) {	// Percorre a tabela TT
				Integer itemID = entry.getKey();										// Obtem
				if(citemID != itemID) {													// Verifica se nao e o mesmo itemID
					LinkedList<Integer> crowset = tt.getRegister(citemID).getRowSet();	// Obtem o rowset do elemento com o rowset alterado
					LinkedList<Integer> rowset = entry.getValue().getRowSet();			// Obtem o rowset do atual itemID do laco "for"
//					// TODO
//					if(citemID.equals(1430) && itemID.equals(1532)) {
//						System.out.println(tt.getRegister(citemID).getRowSet());
//						System.out.println(tt.getRegister(itemID).getRowSet());
//						System.out.println("======");
//						System.out.println(tt.getRegister(citemID).getExcludedRowSet());
//						System.out.println(tt.getRegister(itemID).getExcludedRowSet());
//						System.out.println("\n");
//					}
					if(crowset.size() == rowset.size() && crowset.equals(rowset)) {										// Verifica se os rowsets de ambos são iguais
						
						/* Verifica se ha algum elemento em comum nas excluded rowsets */
						LinkedList<Integer> cexcluded = tt.getRegister(citemID).getExcludedRowSet();
						LinkedList<Integer> excluded = entry.getValue().getExcludedRowSet();
						Boolean hasIntersection = false;
						int i = 0;
						int j = 0;
						int tc = cexcluded.size();
						int te = excluded.size();
						if(tc != 0 && te != 0) {
							while(i < tc && j < te) {
								
								int elemA = cexcluded.get(i);
								int elemB = excluded.get(j);
								if(elemA == elemB) {
									hasIntersection = true;
									break;
								} else if(elemA < elemB) {
									i++;
									continue;
								} else {
									j++;
									continue;
								}
							}
						}
						if(!hasIntersection) {
							toMerge.add(entry.getKey());
							hasAny = true;
						}
					}
				}
			}
			if(hasAny) {													// Caso nao haja intersecao entre os excluded rowsets
				this.tableT.incrementIndex();								// Incrementa o indice atual da tabela T
				int index = this.tableT.getActualIndex();					// Recupera o indice atual
				RegisterTT reg = new RegisterTT(index);						// Gera o novo registro a ser inserido na tabela TT
				reg.setNewRowSet(tt.getRegister(citemID).getRowSet());		// Copia a row set (que sao iguais entre os elementos do merge)
				tt.addRegister(index, reg);									// Adiciona o novo registro a tabela TT
				
				Collections.sort(toMerge);
				int id = toMerge.getFirst();
				String newName = this.tableT.getNamedValue(id);
				changeds.removeFirstOccurrence(id);
				tt.removeRegister(id);
				for(int i=1; i<toMerge.size(); i++) {
					id = toMerge.get(i);
					String nID = this.tableT.getNamedValue(id);
					newName += ","+nID; 
					changeds.removeFirstOccurrence(id);
					tt.removeRegister(id);
				}
				this.tableT.setIndexedValue(index, newName);				// Adiciona o novo nome ao indice
			}
		}
	}
	
//	public HashSet<Integer> findClosedItemSets(TableTT tt) {
//		HashSet<Integer> fci = new HashSet<Integer>();
//		Integer maxSize = 0;
//		for(Map.Entry<Integer, RegisterTT> entry : tt.getTableTT().entrySet()) {
//			RegisterTT reg = entry.getValue();
//			Integer excludedSetSize = reg.getExcludedRowSet().size();
//			if(excludedSetSize == 0) {
//				Integer rowSetSize = reg.getRowSet().size();
//				if(rowSetSize > maxSize) {
//					maxSize = rowSetSize;
//					fci = new HashSet<Integer>();
//					fci.add(entry.getKey());
//				} else if(rowSetSize == maxSize) {
//					fci.add(entry.getKey());
//				}
//			}
//		}
//		return fci;
//	}
	
//	public HashSet<Integer> setClosedItemSets() {
//		this.fci = new HashSet<Integer>();
//		Integer maxSize = 0;
//		if(this.getTableSize() == 1) {
//			RegisterTT reg = this.tableTT.entrySet().iterator().next().getValue();
//			if(reg.getExcludedRowSetSize() == 0){
//				int rowSetSize = reg.getRowSetSize();
//				int lastRID = reg.getLastFromRowSet();
//				if(rowSetSize == lastRID) {
//					this.fci.add(reg.getItemID());
//				}
//			}
//		} else {
//			for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {	// Para cada itemID da tabela TT
//				RegisterTT reg = entry.getValue();									// Obtem o registro correspondente 
//				if(reg.getExcludedRowSetSize() == 0) {								// Se o conjunto excludedRowSet for vazio
//					Integer rowSetSize = reg.getRowSetSize();						// Obtem o tamanho do rowSet deste itemID
//					if(rowSetSize > maxSize) {										// Se o tamanho atual for maior que o maximo anterior
//						maxSize = rowSetSize;										// Atualiza com o novo maior tamanho
//						this.fci = new HashSet<Integer>();							// Gera novo conjunto FCI
//						int lastRID = reg.getLastFromRowSet();
//						if(lastRID == maxSize) {
//							this.fci.add(entry.getKey());							// Adiciona o itemID ao FCI
//						}
//					} else if(rowSetSize == maxSize) {								// Se o tamanho do rowSet for igual ao tamanho maximo
//						int lastRID = reg.getLastFromRowSet();
//						if(lastRID == maxSize) {
//							this.fci.add(entry.getKey());							// Adiciona o itemID ao FCI
//						}
//					}
//				}
//			}
//		}
//		return this.fci;
//	}
	
	public HashSet<Integer> setClosedItemSets() {
		this.fci = new HashSet<Integer>();
		Integer maxSize = 0;
//		this.printTableTT(false);
		if(this.getTableSize().equals(1)) {
			RegisterTT reg = this.tableTT.entrySet().iterator().next().getValue();
			if(reg.getExcludedRowSetSize().equals(0)){
				int rowSetSize = reg.getRowSetSize();
				int lastRID = reg.getLastFromRowSet();
//				System.out.println(reg.getItemID() +" - " + rowSetSize+" - " + lastRID);
//				System.out.println(reg);
				if(rowSetSize == lastRID) {
					this.fci.add(reg.getItemID());
				}
			}
		} else {
			HashSet<RegisterTT> auxSet = new HashSet<RegisterTT>();
			for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {	// Para cada itemID da tabela TT				
				if(entry.getValue().getRowSetSize() > maxSize) {
					maxSize = entry.getValue().getRowSetSize();
				}
			}
			for(Map.Entry<Integer, RegisterTT> entry : this.tableTT.entrySet()) {	// Para cada itemID da tabela TT
//				if(entry.getValue().getItemID().equals( 497 )) {
//				System.out.println(this.tableTT.size() + " - " + this.getY() + " - " + entry.getValue());
//			}
				if(entry.getValue().getRowSetSize() == maxSize && entry.getValue().getExcludedRowSetSize().equals(0)) {
					if(entry.getValue().getLastFromRowSet().equals(maxSize)) {
						this.fci.add((entry.getValue().getItemID()));
					}
				}
			}
		}
		return this.fci;
	}
	
	public HashSet<Integer> getFCI() {
		return this.fci;
	}
	
	public void printFCI() {
		if(this.fci != null) {
			System.out.println(">>> FCI: ");
			for(Integer itemID : this.fci) {
				String name = this.tableT.getNamedValue(itemID);
				System.out.println(name);
			}
		} else {
			System.out.println(">>> FCI: None");
		}
	}
	
	public String printTableTT(Boolean mapped) {
		if(mapped) {
			System.out.println("<<< Mapped Table TT >>>");
		} else {
			System.out.println("<<< Non Mapped Table TT >>>");
		}
		System.out.println("Excluded: " + this.excludedRowSetTT);
		System.out.println("Skipped: " + this.skippedRowSetTT);
		System.out.println("Y: " + this.getY());
		List<Integer> keys = new ArrayList<Integer>(this.tableTT.keySet());
		Collections.sort(keys);
		String msg = "";
		for(int i=0; i<keys.size(); i++) {
			String itemId;
			if(mapped) {
				itemId = Integer.toString(keys.get(i));
			} else {
				itemId = this.tableT.getNamedValue(keys.get(i));
			}
			msg += itemId;
			msg += " - ";
			msg += this.tableTT.get(keys.get(i));
			msg += "\n";
			System.out.println(itemId + " - " + this.tableTT.get(keys.get(i)));
			try {
				Thread.sleep((long) 10.0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.printFCI();
		return msg;
	}
	
	
	
}
