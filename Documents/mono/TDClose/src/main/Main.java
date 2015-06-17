package main;

import tdclose.TDClose;
import tools.MemoryLogger;

public class Main {

	public static void main(String[] args) {
		System.out.println("START");
		
		Integer test = 0;
		
		String filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/input001";
//		String filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/data_set_ALL_AML_train_edited";
//		String filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/D10T5C3-1_edited";
//		String filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/D400T50C10-1_edited";
//		String filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/D4000T100C10-1_edited";
		
		
//		Integer minSup = 2852;
		Integer minSup = 1;
		
		long startTimestamp; // start time of last execution
		long endTimestamp; // end time of last execution
		
		System.out.println("Teste: " + test);
		if(test == 0) {
			filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/input001";
			minSup = 2;
			
			TDClose tdclose = new TDClose();
			tdclose.setupTableT(filePath);
			tdclose.setMinSup(minSup);
			tdclose.setupTableTT();
			tdclose.setThreshold();
			
//			tdclose.printTableT();
//			tdclose.printTableTT();
			
			// reset the utility for checking the memory usage
			MemoryLogger.getInstance().reset();
			// record the start time
			startTimestamp = System.currentTimeMillis();
			
			/* RUN ALGORITHM */
			tdclose.topDownMine(tdclose.getTableTT());
			
			// record end time
			endTimestamp = System.currentTimeMillis();
			// check the memory usage
			MemoryLogger.getInstance().checkMemory();
			
			/* PRINT STATS */
			tdclose.printFCI();
			System.out.println("\n\n=============  TD-CLOSE - STATS =============");
			System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
			System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
			
		} else if(test == 1) {
			filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/D400T50C10-1_edited";
			minSup = 12;
			
			TDClose tdclose = new TDClose();
			tdclose.setupTableT(filePath);
			tdclose.setMinSup(minSup);
			tdclose.setupTableTT();
			tdclose.setThreshold();
			
//			tdclose.printTableT();
//			tdclose.printTableTT();
			
			// reset the utility for checking the memory usage
			MemoryLogger.getInstance().reset();
			// record the start time
			startTimestamp = System.currentTimeMillis();
			
			/* RUN ALGORITHM */
			tdclose.topDownMine(tdclose.getTableTT());

			// record end time
			endTimestamp = System.currentTimeMillis();
			// check the memory usage
			MemoryLogger.getInstance().checkMemory();
			
			
			/* PRINT STATS */
			tdclose.printFCI();
			System.out.println("\n\n=============  TD-CLOSE - STATS =============");
			System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
			System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
			
			
		} else if(test == 3) {
			filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/D8000T50C4-1_edited";
			minSup = 16;
			
			TDClose tdclose = new TDClose();
			tdclose.setupTableT(filePath);
			tdclose.setMinSup(minSup);
			tdclose.setupTableTT();
			tdclose.setThreshold();
			
//			tdclose.printTableT();
//			tdclose.printTableTT();
			
			// reset the utility for checking the memory usage
			MemoryLogger.getInstance().reset();
			// record the start time
			startTimestamp = System.currentTimeMillis();
			
			/* RUN ALGORITHM */
			tdclose.topDownMine(tdclose.getTableTT());

			// record end time
			endTimestamp = System.currentTimeMillis();

			// check the memory usage
			MemoryLogger.getInstance().checkMemory();
			
			
			/* PRINT STATS */
			tdclose.printFCI();
			System.out.println("\n\n=============  TD-CLOSE - STATS =============");
			System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
			System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
			
		} else if(test == 4) {
			filePath = "/home/lauro/Documents/eclipseWorkspace/TDClose/data_set_ALL_AML_independent_edited04.csv";
			minSup = 3;
			
			TDClose tdclose = new TDClose();
			tdclose.setupTableT(filePath);
			tdclose.setMinSup(minSup);
			tdclose.setupTableTT();
			tdclose.setThreshold();
			
//			tdclose.printTableT();
//			tdclose.printTableTT();
			
			// reset the utility for checking the memory usage
			MemoryLogger.getInstance().reset();
			// record the start time
			startTimestamp = System.currentTimeMillis();
			
			/* RUN ALGORITHM */
			tdclose.topDownMine(tdclose.getTableTT());

			// record end time
			endTimestamp = System.currentTimeMillis();

			// check the memory usage
			MemoryLogger.getInstance().checkMemory();
			
			
			/* PRINT STATS */
			tdclose.printFCI();
			System.out.println("\n\n=============  TD-CLOSE - STATS =============");
			System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
			System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
			
		} else {
			System.out.println("Não há este teste...");
//			try {
//			 
//			String content = "This is the content to write into file";
//
//			File file = new File("/home/lauro/Documents/eclipseWorkspace/TDClose/out001");
//
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(a);
//			bw.close();
//
//			System.out.println("Done");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		}
		
		System.out.println("\n\n===================  END  ===================");
	}

}
