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
	public static int maxiDSnum = 30;
	public static int miniDSnum = 30;
	public static double minDsSize = 17.6;
	public static double maxDsSize = 26.4;

	public static int maxCopyno = 1;
	public static int minCopyno = 1;

	public static int maxTnum = 15;
	public static int minTnum = 15;
	public static int maxIDS = 13;
	public static int minIDS = 4;

	public static double lamda = 0.87723824;
	public static double variation = 0.23423242;
	public static double chiasma = 0.88738278;
	public static double variance = 0.001;

	public static int genSize = 500;
	public static int popSize = 50;
	public static int maxGen = 100;

	public static String FOLDER = "DataReplication/";
	public static String CONFIGURATION = "configuration";
	public static String DATASETS = "datasets.xml";
	public static String nDATASETS = "ndatasets.xml";
	public static String CONTROL = "control.xml";
	public static String CLOUD = "cloud.xml";
	public static HashMap<String, String> configerationhMap;
	public static String inputFolder;
	public static String outputFolder;
}