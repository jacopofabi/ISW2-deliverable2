package dataset;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import tools.CSVWriter;
import tools.Parameters;
import weka.WekaProject;
import weka.WekaResult;
import weka.core.Instances;

 /*
  * Classe che si occupa della parte di analisi del dataset attraverso la libreria di Weka
  */
public class DatasetAnalyzer {
	
	public void analyze(String projectName) {		
		Logger logger = Logger.getLogger(DatasetAnalyzer.class.getName());
		logger.log(Level.INFO,"Loading Dataset");
		
		WekaProject weka = new WekaProject(projectName);
		
		/*
		 * Conversione CSV - ARFF
		 */
		CSVWriter.convertCSVtoARFF(Parameters.OUTPUT_PATH + projectName + Parameters.WEKA_CSV);
		Instances data = CSVWriter.loadFileARFF(Parameters.OUTPUT_PATH + projectName + Parameters.DATASET_ARFF);
		weka.setDataset(data);
		
		File file = new File(Parameters.OUTPUT_PATH + projectName + Parameters.WEKA_CSV);
		if(file.exists()) {
			file.deleteOnExit();
		}
		
		//esecuzione di walk forward per tutti i tipi di classificatori, metodi di feature selection e metodi di balancing
		logger.log(Level.INFO,"Computing results...");
		List<WekaResult> resultList = weka.runWalkForward();
		
		//scrittura dei risultati su un file CSV		
		logger.log(Level.INFO,"Writing CSV...");
		CSVWriter.writeResultOnCSV(resultList, projectName, Parameters.RESULT_CSV);
		
		logger.log(Level.INFO,"CSV written successfully.\nEnd of the program.");
	}
	
	public static void main(String[] args) {
		DatasetAnalyzer analyzer = new DatasetAnalyzer();
		String project1 = Parameters.BOOKKEEPER;
		String project2 = Parameters.AVRO;
		Parameters.setParameters(project1);
		analyzer.analyze(project1);

		Parameters.setParameters(project2);
		analyzer.analyze(project2);
		
	}
}
