package strategies;

import java.util.HashMap;

/**
 * @author Admin
 */
public final class R {
	// 数据中心
	public static int maxDCnum = 15;
	public static int minDCnum = 15;
	public static double maxDCstorage = 1000;
	public static double minDCstorage = 1000;
	// 带宽
	public static double maxBandWidth = 10;
	public static double minBandWith = 5;
	// 数据集
	public static int maxiDSnum = 20;
	public static int miniDSnum = 20;
	public static double minDsSize = 17.6;
	public static double maxDsSize = 26.4;

	public static int maxCopyno = 2;
	public static int minCopyno = 2;

	public static int maxTnum = 6;
	public static int minTnum = 6;
	public static int maxIDS = 13;
	public static int minIDS = 4;

	public static double lamda = 0.87723824;
	public static double variation = 0.23423242;
	public static double chiasma = 0.88738278;

	// 程序退出控制条件
	public static double variance = 0.001;
	public static double speed = 0.001;
	public static int var_and_speed_Gen = 10;
	public static int speed_Gen = 50;
	public static int minGen = 50;

	public static int genSize = 1000;
	public static int popSize = 100;
	public static int maxGen = 10000;

	public static String FOLDER = "DataReplication/";
	public static String CONFIGURATION = "configuration";
	public static String DATASETS = "datasets.xml";
	public static String NDATASETS = "ndatasets.xml";
	public static String CONTROL = "control.xml";
	public static String CLOUD = "cloud.xml";
	public static HashMap<String, String> configerationhMap = null;
	public static String inputFolder = null;
	public static String outputFolder = null;

	/**
	 * @return the maxDCnum
	 */
	public static int getMaxDCnum() {
		return maxDCnum;
	}

	/**
	 * @return the minDCnum
	 */
	public static int getMinDCnum() {
		return minDCnum;
	}

	/**
	 * @return the maxDCstorage
	 */
	public static double getMaxDCstorage() {
		return maxDCstorage;
	}

	/**
	 * @return the minDCstorage
	 */
	public static double getMinDCstorage() {
		return minDCstorage;
	}

	/**
	 * @return the minGen
	 */
	public static int getMinGen() {
		return minGen;
	}

	/**
	 * @return the maxBandWidth
	 */
	public static double getMaxBandWidth() {
		return maxBandWidth;
	}

	/**
	 * @return the minBandWith
	 */
	public static double getMinBandWith() {
		return minBandWith;
	}

	/**
	 * @return the maxiDSnum
	 */
	public static int getMaxiDSnum() {
		return maxiDSnum;
	}

	/**
	 * @return the miniDSnum
	 */
	public static int getMiniDSnum() {
		return miniDSnum;
	}

	/**
	 * @return the minDsSize
	 */
	public static double getMinDsSize() {
		return minDsSize;
	}

	/**
	 * @return the maxDsSize
	 */
	public static double getMaxDsSize() {
		return maxDsSize;
	}

	/**
	 * @return the maxCopyno
	 */
	public static int getMaxCopyno() {
		return maxCopyno;
	}

	/**
	 * @return the minCopyno
	 */
	public static int getMinCopyno() {
		return minCopyno;
	}

	/**
	 * @return the maxTnum
	 */
	public static int getMaxTnum() {
		return maxTnum;
	}

	/**
	 * @return the minTnum
	 */
	public static int getMinTnum() {
		return minTnum;
	}

	/**
	 * @return the maxIDS
	 */
	public static int getMaxIDS() {
		return maxIDS;
	}

	/**
	 * @return the minIDS
	 */
	public static int getMinIDS() {
		return minIDS;
	}

	/**
	 * @return the lamda
	 */
	public static double getLamda() {
		return lamda;
	}

	/**
	 * @return the variation
	 */
	public static double getVariation() {
		return variation;
	}

	/**
	 * @return the chiasma
	 */
	public static double getChiasma() {
		return chiasma;
	}

	/**
	 * @return the variance
	 */
	public static double getVariance() {
		return variance;
	}

	/**
	 * @return the speed
	 */
	public static double getSpeed() {
		return speed;
	}

	/**
	 * @return the var_and_speed_Gen
	 */
	public static int getVar_and_speed_Gen() {
		return var_and_speed_Gen;
	}

	/**
	 * @return the speed_Gen
	 */
	public static int getSpeed_Gen() {
		return speed_Gen;
	}

	/**
	 * @return the genSize
	 */
	public static int getGenSize() {
		return genSize;
	}

	/**
	 * @return the popSize
	 */
	public static int getPopSize() {
		return popSize;
	}

	/**
	 * @return the maxGen
	 */
	public static int getMaxGen() {
		return maxGen;
	}

	/**
	 * @return the fOLDER
	 */
	public static String getFOLDER() {
		return FOLDER;
	}

	/**
	 * @return the cONFIGURATION
	 */
	public static String getCONFIGURATION() {
		return CONFIGURATION;
	}

	/**
	 * @return the dATASETS
	 */
	public static String getDATASETS() {
		return DATASETS;
	}

	/**
	 * @return the nDATASETS
	 */
	public static String getNDATASETS() {
		return NDATASETS;
	}

	/**
	 * @return the cONTROL
	 */
	public static String getCONTROL() {
		return CONTROL;
	}

	/**
	 * @return the cLOUD
	 */
	public static String getCLOUD() {
		return CLOUD;
	}

	/**
	 * @return the configerationhMap
	 */
	public static HashMap<String, String> getConfigerationhMap() {
		return configerationhMap;
	}

	/**
	 * @return the inputFolder
	 */
	public static String getInputFolder() {
		return inputFolder;
	}

	/**
	 * @return the outputFolder
	 */
	public static String getOutputFolder() {
		return outputFolder;
	}

}