package weka;

import weka.classifiers.evaluation.Evaluation;
import weka.core.Instance;
import weka.core.Instances;


public class WekaResult {
	double tp;
	double fp;
	double tn;
	double fn;
	double precision;
	double recall;
	double auc;
	double kappa;
	
	private String projName;
	private int numTrainingRelease;
	private double percentageTraining;
	private double percentageBuggyInTraining;
	private double percentageBuggyInTesting;
	
	private String classifierName;
	private String featureSelectionName;
	private String resamplingMethodName;
	
	public WekaResult(String classifierName, String featureSelectionName, String resamplingMethodName) {
		super();
		this.setClassifierName(classifierName);
		this.setFeatureSelectionName(featureSelectionName);
		this.setResamplingMethodName(resamplingMethodName);
	}

	
	/**
	 * Salva le informazioni del dataset.
	 * Imposta il numero di release in training e la percentuale di buggyness nel training e nel test set
	 * */
	public void setDatasetValues(Instances training, Instances test, int testReleaseIndex) {
		this.numTrainingRelease = testReleaseIndex - 1;
		
		int numInstancesTraining = training.numInstances();
		int numInstancesTest = test.numInstances();
		this.percentageTraining = (double)numInstancesTraining/(double)(numInstancesTraining+numInstancesTest);
		
		int numBuggyTraining = 0;
		int numFeatures = training.numAttributes();
		for(Instance instance: training) {
			if( ((String)instance.stringValue(numFeatures-1)).equalsIgnoreCase("true")) {
				numBuggyTraining = numBuggyTraining + 1;
			}
		}
		
		int numBuggyTest = 0;
		for(Instance instance: test) {
			if( ((String)instance.stringValue(numFeatures-1)).equalsIgnoreCase("true")) {
				numBuggyTest = numBuggyTest + 1;
			}
		}
		this.percentageBuggyInTraining = (double)numBuggyTraining/(double)numInstancesTraining;
		this.percentageBuggyInTesting = (double)numBuggyTest/(double)numInstancesTest;
	}
	
	/**
	 * Imposta i valori delle varie metriche usate per il performance evaluation.
	 * Nei casi limiti si seguono le indicazioni qui ripostate https://github.com/dice-group/gerbil/wiki/Precision,-Recall-and-F1-measure
	 * */
	public void setValues(Evaluation eval, int positiveClassIndex) {
		tp = eval.numTruePositives(positiveClassIndex);
		fp = eval.numFalsePositives(positiveClassIndex);
		tn = eval.numTrueNegatives(positiveClassIndex);
		fn = eval.numFalseNegatives(positiveClassIndex);
		if(tp == 0 && fp == 0 && fn == 0) {
			this.precision = 1;
			this.recall = 1;
		} else if (tp == 0 && (fp > 0 || fn > 0)) {
			this.precision = 0;
			this.recall = 0;
		} else {
			this.precision = eval.precision(positiveClassIndex);
			this.recall = eval.recall(positiveClassIndex);
			this.auc = eval.areaUnderROC(positiveClassIndex);
			this.kappa = eval.kappa();
		}
	}
	
	public String toString() {
		String str = "";
		str = str + "TP: " + tp + " - FP: "+ fp + "\nFN: "+ fn + " - TN: " + tn+ "\n"
				+ "precision: " + precision +"\nrecall: " + recall + "\nauc: "+ auc +"\nkappa: "+ kappa;
		return str;
	}
	
	
	
	/*===============================================================================================
	 * Getters & Setters
	 */
	
	public double getTP() {
		return tp;
	}

	public void setTP(double tP) {
		tp = tP;
	}

	public double getFP() {
		return fp;
	}

	public void setFP(double fP) {
		fp = fP;
	}

	public double getTN() {
		return tn;
	}

	public void setTN(double tN) {
		tn = tN;
	}

	public double getFN() {
		return fn;
	}

	public void setFN(double fN) {
		fn = fN;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getAuc() {
		return auc;
	}

	public void setAuc(double auc) {
		this.auc = auc;
	}

	public double getKappa() {
		return kappa;
	}

	public void setKappa(double kappa) {
		this.kappa = kappa;
	}
	
	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public int getNumTrainingRelease() {
		return numTrainingRelease;
	}

	public void setNumTrainingRelease(int numTrainingRelease) {
		this.numTrainingRelease = numTrainingRelease;
	}

	public double getPercentageTraining() {
		return percentageTraining;
	}

	public void setPercentageTraining(double percentageTraining) {
		this.percentageTraining = percentageTraining;
	}

	public double getPercentageBuggyInTraining() {
		return percentageBuggyInTraining;
	}

	public void setPercentageBuggyInTraining(double percentageBuggyInTraining) {
		this.percentageBuggyInTraining = percentageBuggyInTraining;
	}

	public double getPercentageBuggyInTesting() {
		return percentageBuggyInTesting;
	}

	public void setPercentageBuggyInTesting(double percentageBuggyInTesting) {
		this.percentageBuggyInTesting = percentageBuggyInTesting;
	}
	
	public String getFeatureSelectionName() {
		return featureSelectionName;
	}

	public void setFeatureSelectionName(String featureSelectionName) {
		this.featureSelectionName = featureSelectionName;
	}

	public String getResamplingMethodName() {
		return resamplingMethodName;
	}

	public void setResamplingMethodName(String resamplingMethodName) {
		this.resamplingMethodName = resamplingMethodName;
	}

	public String getClassifierName() {
		return classifierName;
	}

	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}
}
