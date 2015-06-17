/**
 * 
 */
package table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Lauro Moraes
 *
 */
public class TableT {
	
	protected Map<Integer, ArrayList<String>> orginalTable;
	protected Map<Integer, ArrayList<Integer>> table;
	protected Map<String, Integer> nameToIndexMap;
	protected Map<Integer, String> indexToNameMap;
	protected Integer index;
	List<String> l = new LinkedList<String>();

	public TableT() {
		this.orginalTable	= new HashMap<Integer, ArrayList<String>>();
		this.table			= new HashMap<Integer, ArrayList<Integer>>();
		this.nameToIndexMap	= new HashMap<String, Integer>();
		this.indexToNameMap	= new HashMap<Integer, String>();
		this.index			= 0;
	}
	
	public void readTableFromFile(String filePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath)); // Abre buffer de leitura
			String currentLine = null;
			int cnt = 1;
			while((currentLine = br.readLine())!=null) { // Le cada uma das linhas do arquivo
				if(currentLine.length() > 1) {
					this.putLine(cnt++, currentLine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(br!=null)
					br.close(); // Fecha buffer de leitura
			} catch (Exception e2) {e2.printStackTrace();}
		}
	}
	
	public void putLine(Integer rowId, String line) {
		try {
			ArrayList<String> row = new ArrayList<String>(Arrays.asList(line.split("[ ,;]")));
			this.orginalTable.put(rowId, row);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public ArrayList<Integer> getLine(Integer index) {
		return this.table.get(index);
	}
	
	public void mapValues() {
		List<Integer> keys = new ArrayList<Integer>(this.orginalTable.keySet());
		Collections.sort(keys);
		for(int i=0; i<keys.size(); i++) {											// Para cada linha da tabela
			int k = keys.get(i);													// Recupera o indice atual da linha da tabela
			ArrayList<String> itemset = this.orginalTable.get(k);					// Recupera a linha da tabela pelo indice
			ArrayList<Integer> newItemset = new ArrayList<Integer>(itemset.size());	// Cria nova lista para comportar os itens mapeados
			for(String item : itemset) {											// Para cada elemento de uma linha da tabela
				int ind;
				if(!this.nameToIndexMap.containsKey(item)){							// Verifica se o item ja esta mapeado
					ind = ++this.index;												// Gera novo identificador unico para o item
					this.nameToIndexMap.put(item, ind);								// Mapeia item para identificador
					this.indexToNameMap.put(ind, item);								// Mapeia identificador para item
				} else {
					ind = this.nameToIndexMap.get(item);							// Recupera o identificador do item
				}
				newItemset.add(ind);												// Adiciona o idenficador no novo itemset
			}
			this.table.put(k, newItemset);											// Adiciona o novo itemset com valores mapeados
		}
	}
	
	public void mapValuesInteger() {
		List<Integer> keys = new ArrayList<Integer>(this.orginalTable.keySet());
		Collections.sort(keys);
		for(int i=0; i<keys.size(); i++) {											// Para cada linha da tabela
			int k = keys.get(i);													// Recupera o indice atual da linha da tabela
			ArrayList<String> itemset = this.orginalTable.get(k);					// Recupera a linha da tabela pelo indice
			ArrayList<Integer> newItemset = new ArrayList<Integer>(itemset.size());	// Cria nova lista para comportar os itens mapeados
			for(String item : itemset) {											// Para cada elemento de uma linha da tabela
				int ind;
				if(!this.nameToIndexMap.containsKey(item)){							// Verifica se o item ja esta mapeado
					++this.index;
					ind = Integer.parseInt(item);
					this.nameToIndexMap.put(item, ind);								// Mapeia item para identificador
					this.indexToNameMap.put(ind, item);								// Mapeia identificador para item
				} else {
					ind = this.nameToIndexMap.get(item);							// Recupera o identificador do item
				}
				newItemset.add(ind);												// Adiciona o idenficador no novo itemset
			}
			this.table.put(k, newItemset);											// Adiciona o novo itemset com valores mapeados
		}
	}
	
	public Integer getActualIndex() {
		return this.index;
	}
	
	public Integer getTableSize() {
		return this.table.size();
	}
	
	public void incrementIndex() {
		this.index++;
	}
	
	public void setIndexedValue(Integer index, String name) {
		this.indexToNameMap.put(index, name);
	}
	
	public Integer getIndexedValue(String name) {
		return this.nameToIndexMap.get(name);
	}
	
	public String getNamedValue(Integer index) {
		return this.indexToNameMap.get(index);
	}
	
	public Map<Integer, ArrayList<Integer>> getTable() {
		return this.table;
	}
	
	public void printOriginalTable() {
		System.out.println("<<< Original Table T >>>");
		List<Integer> keys = new ArrayList<Integer>(this.orginalTable.keySet());
		Collections.sort(keys);
		for(int i=0; i<keys.size(); i++) {
			System.out.println(keys.get(i) + " - " + this.orginalTable.get(keys.get(i)));
		}
	}
	
	public void printMappedTable() {
		System.out.println("<<< Mapped Table T >>>");
		List<Integer> keys = new ArrayList<Integer>(this.table.keySet());
		Collections.sort(keys);
		for(int i=0; i<keys.size(); i++) {
			System.out.println(keys.get(i) + " - " + this.table.get(keys.get(i)));
		}
	}

}
