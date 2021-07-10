package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.WekaResult;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import data.Metrics;
import data.ProjectClass;
import git.GitCommit;
import git.GitRelease;
import jira.JiraRelease;
import jira.JiraTicket;

public class CSVWriter {

	private CSVWriter() {
	}

	/**
	 * [DEBUG] Salva su un .csv i dati delle Release
	 */
	public static void writeReleasesOnCSV(List<GitRelease> releases, String projectName, String fileName) {

		String outputName = Parameters.OUTPUT_PATH + projectName + fileName + ".csv";

		try (FileWriter fileWriter = new FileWriter(outputName)) {
			StringBuilder outputBuilder = new StringBuilder("ID;Version Name;Release Date\n");

			for (GitRelease r : releases) {
				outputBuilder.append(r.getId() + ";" + r.getName() + ";" + r.getDate() + "\n");
			}
			fileWriter.append(outputBuilder.toString());

		} catch (Exception e) {
			Logger logger = Logger.getLogger(CSVWriter.class.getName());
			logger.log(Level.SEVERE,Parameters.CSV_ERROR, e);
		}
	}

	/**
	 * [DEBUG] Salva su un .csv i dati dei Commit
	 */
	public static void writeCommitsOnCSV(List<GitCommit> commits, String projectName, String fileName) {

		String outputName = Parameters.OUTPUT_PATH + projectName + fileName + ".csv";

		try (FileWriter fileWriter = new FileWriter(outputName)) {
			StringBuilder outputBuilder = new StringBuilder("Index;Date;Message\n");

			for (GitCommit c : commits) {
				outputBuilder.append(c.getId() + ";" + c.getDate() + ";" + c.getMessage() + "\n");
			}
			fileWriter.append(outputBuilder.toString());
			System.out.println(outputName + "\tSAVED");

		} catch (Exception e) {
			Logger logger = Logger.getLogger(CSVWriter.class.getName());
			logger.log(Level.SEVERE, Parameters.CSV_ERROR, e);
		}
	}

	/**
	 * [DEBUG] Salva su un .csv i dati dei JiraTicket
	 */
	public static void writeTicketOnCsv(List<JiraTicket> tickets, String projectName, String fileName) {

		String outputName = Parameters.OUTPUT_PATH + projectName + fileName + ".csv";

		try (FileWriter fileWriter = new FileWriter(outputName)) {
			StringBuilder outputBuilder = new StringBuilder(
					"Name;CreationDate;ResolutionDate;AffectedVersions;InjectedVersion;FixedVersion\n");

			for (JiraTicket t : tickets) {
				outputBuilder.append(t.getName() + ";" + t.getCreationDate() + ";" + t.getResolutionDate() + ";");
				for (JiraRelease j : t.getAffectedVersions()) {
					outputBuilder.append(j.getName() + " ");
				}
				outputBuilder.append(";");
				outputBuilder.append(t.getIv().getName() + ";");
				outputBuilder.append(t.getFv().getName() + " ");
				outputBuilder.append("\n");
			}
			fileWriter.append(outputBuilder.toString());

		} catch (Exception e) {
			Logger logger = Logger.getLogger(CSVWriter.class.getName());
			logger.log(Level.SEVERE, Parameters.CSV_ERROR, e);
		}
	}
	
	/*
	 * Crea il Dataset scrivendo su un file .csv la lista delle classi con le relative metriche.
	 */
	public static void writeClassOnCSV(List<ProjectClass> classes, String projectName, String fileName) {
        Logger logger = Logger.getLogger(CSVWriter.class.getName());

        try (FileWriter fileWriter = new FileWriter(Parameters.OUTPUT_PATH + projectName + fileName)) {
            StringBuilder outputBuilder = new StringBuilder(
                    "VersionID;VersionName;Path;Size;LOC_Touched;AVGLocAdded;LocAdded;MaxLocAdded;Churn;MaxChurn;ChgSetSize;MaxChgSetSize;AVGChgSetSize;NumRevisions;NumBugFixed;Age;Buggyness\n");

            for (ProjectClass c : classes) {
            	Metrics metrics = c.getMetrics();
                outputBuilder.append(c.getRelease().getId() + ";" + 
                					 c.getRelease().getName() + ";" + 
                					 c.getPath() + ";" + 
                					 metrics.getSize() + ";" + 
                					 metrics.getLocTouched() + ";" + 
                					 metrics.getAvgLocAdded() + ";"+
                					 metrics.getLocAdded() + ";"+
                					 metrics.getMaxLocAdded() + ";"+
                					 metrics.getChurn() + ";"+
                					 metrics.getMaxChurn() + ";"+
                					 metrics.getChgSetSize() + ";"+
                					 metrics.getMaxChgSetSize() + ";"+
                					 metrics.getAvgChgSetSize() + ";"+
                					 metrics.getNumberRevisions() + ";"+
                					 metrics.getNumberBugFixes() + ";"+
                					 metrics.getAge() + ";" + 
                					 c.isBuggy());
                outputBuilder.append("\n");
            }
            fileWriter.append(outputBuilder.toString());

        } catch (Exception e) {
            logger.log(Level.SEVERE, Parameters.CSV_ERROR, e);
        }
    } 
	
	/*
	 * Crea un file .csv dove le colonne sono separate tramite "," così da evidenziare gli attributi per Weka e creare un ARFF corretto
	 */
	public static void writeCSVForWeka(List<ProjectClass> classes, String projectName, String fileName) {
        Logger logger = Logger.getLogger(CSVWriter.class.getName());

        try (FileWriter fileWriter = new FileWriter(Parameters.OUTPUT_PATH + projectName + fileName)) {
            StringBuilder outputBuilder = new StringBuilder(
                    "VersionID,VersionName,Path,Size,LOC_Touched,AVGLocAdded,LocAdded,MaxLocAdded,Churn,MaxChurn,ChgSetSize,MaxChgSetSize,AVGChgSetSize,NumRevisions,NumBugFixed,Age,Buggyness\n");

            for (ProjectClass c : classes) {
            	Metrics metrics = c.getMetrics();
                outputBuilder.append(c.getRelease().getId() + "," + 
                					 c.getRelease().getName() + "," + 
                					 c.getPath() + "," + 
                					 metrics.getSize() + "," + 
                					 metrics.getLocTouched() + "," + 
                					 metrics.getAvgLocAdded() + ","+
                					 metrics.getLocAdded() + ","+
                					 metrics.getMaxLocAdded() + ","+
                					 metrics.getChurn() + ","+
                					 metrics.getMaxChurn() + ","+
                					 metrics.getChgSetSize() + ","+
                					 metrics.getMaxChgSetSize() + ","+
                					 metrics.getAvgChgSetSize() + ","+
                					 metrics.getNumberRevisions() + ","+
                					 metrics.getNumberBugFixes() + ","+
                					 metrics.getAge() + "," + 
                					 c.isBuggy());
                outputBuilder.append("\n");
            }
            fileWriter.append(outputBuilder.toString());

        } catch (Exception e) {
            logger.log(Level.SEVERE, Parameters.CSV_ERROR, e);
        }
    } 
	
	/*
	 * Scrive su un file .csv i risultati ottenuti tramite Weka.
	 */
	public static void writeResultOnCSV(List<WekaResult> results, String projectName, String fileName) {
		Logger logger = Logger.getLogger(CSVWriter.class.getName());

        try (FileWriter fileWriter = new FileWriter(Parameters.OUTPUT_PATH + projectName + fileName)) {
            StringBuilder outputBuilder = new StringBuilder(
            		"Progetto;#Training Release;%Training;%Buggy in training;%Buggy in test;Classifier;Feature selection;"
            				+ "Balancing;TP;FP;TN;FN;Precision;Recall;Area Under ROC;Kappa\n");
            for (WekaResult r : results) {
                outputBuilder.append(projectName + ";" + 
                					 r.getNumTrainingRelease() + ";" + 
                					 String.format(Locale.US, "%.2f", r.getPercentageTraining()) + ";" + 
                					 String.format(Locale.US, "%.2f", r.getPercentageBuggyInTraining()) + ";" + 
                					 String.format(Locale.US, "%.2f", r.getPercentageBuggyInTesting()) + ";" + 
                					 r.getClassifierName() + ";"+
                					 r.getFeatureSelectionName() + ";"+
                					 r.getResamplingMethodName() + ";"+
                					 r.getTP() + ";"+
                					 r.getFP() + ";"+
                					 r.getTN() + ";"+
                					 r.getFN() + ";"+
                					 String.format(Locale.US, "%.2f", r.getPrecision()) + ";"+
                					 String.format(Locale.US, "%.2f", r.getRecall()) + ";"+
                					 String.format(Locale.US, "%.2f", r.getAuc()) + ";"+
                					 String.format(Locale.US, "%.2f", r.getKappa()));
                outputBuilder.append("\n");
            }
            fileWriter.append(outputBuilder.toString());

        } catch (Exception e) {
            logger.log(Level.SEVERE, Parameters.CSV_ERROR, e);
        }
    } 
	
	/*
	 * Converte un file CSV in un file ARFF da fornire a Weka.
	 */
	public static void convertCSVtoARFF(String fileName) {
	    try {
			// load CSV
		    Instances data = loadFileCSV(fileName);
		    // save ARFF
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(new File(fileName.replace(Parameters.WEKA_CSV, Parameters.DATASET_ARFF)));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Carica il file CSV contenente il dataset ed elimina le colonne inutili alla stima (feature selection preventiva)
	 * L'ID della release viene eliminato successivamente perchè necessario nell'iterazione di walk forward
	 */
	private static Instances loadFileCSV(String fileName) {
		CSVLoader loader = new CSVLoader();
		Instances data = null;
		int index = 0;
    	try {
			loader.setSource(new File(fileName));
	    	data = loader.getDataSet();
	    	
	    	//elimino le colonne relative al nome della versione e al nome del file
	    	index = data.attribute("VersionName").index();
	    	data.deleteAttributeAt(index);
	    	index = data.attribute("Path").index();
			data.deleteAttributeAt(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/*
	 * Carica il file ARFF contenente il dataset
	 */
	public static Instances loadFileARFF(String fileName) {
		DataSource source;
		Instances data = null;
		try {
			source = new DataSource(fileName);
		    data = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}
