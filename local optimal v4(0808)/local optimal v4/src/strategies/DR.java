package strategies;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import newClass.Cloud;
import newClass.Cloud.DataCenter;
import newClass.DataSets;
import newClass.DataSets.DataSet;
import newClass.Tasks;
import newClass.Tasks.Task;

/**
 * @author Admin use git
 */
public class DR {
	static DataSets dataSets = DataSets.getInstanceofDataSets();
	static Tasks tasks = Tasks.getInstanceofTasks();
	static Cloud cloud = Cloud.getInstanceofCloud();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// ==================本文策略==================
		dataSets = DataSets.getNewInstanceofDataSets();
		tasks = Tasks.getNewInstanceofTasks();
		cloud = Cloud.getNewInstanceofCloud();
		System.err.println("Start & CreateData");

		System.err.println("ReadData");
		readandwrite.readDatas();
		System.err.println("ReadData finished");

		Strategy.initialize();

		System.err.println("The Heredity Begin!");
		ArrayList<Strategy.S> CH = Strategy.Heredity();
		System.err.println("The Heredity End!");

		readandwrite.OutputTheResult(CH);
	}

	public static class readandwrite {
		static Scanner scanner;

		public static void readConfiguration() {
			FileReader fReader = null;
			try {
				fReader = new FileReader(R.FOLDER + R.CONFIGURATION);
				scanner = new Scanner(fReader);
				R.configerationhMap = new HashMap<String, String>();
				while (scanner.hasNext()) {
					R.configerationhMap.put(scanner.next(), scanner.next());
				}
				R.inputFolder = R.configerationhMap.get("inputdatafolder")
						+ "/";
				R.outputFolder = R.configerationhMap.get("outputdatafolder")
						+ "/";
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					fReader.close();
					scanner.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public static void readDatas() {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			NodeList datasetNodeList = null;
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom;
				try {
					dom = db.parse(R.FOLDER + R.inputFolder + R.DATASETS);
					if (dom != null) {
						Element docEle = dom.getDocumentElement();
						datasetNodeList = docEle
								.getElementsByTagName("dataset");
						constructData(datasetNodeList);
					}
					dom = null;
					dom = db.parse(R.FOLDER + R.inputFolder + R.CONTROL);
					if (dom != null) {
						Element element = dom.getDocumentElement();
						datasetNodeList = element.getElementsByTagName("task");
						constructControl(datasetNodeList);
					}
					dom = null;
					dom = db.parse(R.FOLDER + R.inputFolder + R.CLOUD);
					if (dom != null) {
						Element element = dom.getDocumentElement();
						NodeList dcnodList = element
								.getElementsByTagName("datacenter");
						constructCloud(dcnodList);
						NodeList bwnoNodeList = element
								.getElementsByTagName("bandwidth");
						constructBandWidth(bwnoNodeList);

					}
				} catch (FileNotFoundException ex) {
					System.out.println("DDG xml file is not found.");
				}
			} catch (ParserConfigurationException pce) {
				System.out.println("Error while parsing the xml.");
			} catch (SAXException se) {
				System.out.println("Error while parsing the xml.");
			} catch (IOException ioe) {
				System.out.println("Exception while reading the xml.");
			}
		}

		public static void readDatas2() {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			NodeList datasetNodeList = null;

			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom;
				try {
					dom = db.parse(R.FOLDER + R.inputFolder + R.nDATASETS);
					if (dom != null) {
						Element docEle = dom.getDocumentElement();
						datasetNodeList = docEle
								.getElementsByTagName("dataset");
						constructData(datasetNodeList);
					}
					dom = null;
					dom = db.parse(R.FOLDER + R.inputFolder + R.CONTROL);
					if (dom != null) {
						Element element = dom.getDocumentElement();
						datasetNodeList = element.getElementsByTagName("task");
						constructControl(datasetNodeList);
					}
					dom = null;
					dom = db.parse(R.FOLDER + R.inputFolder + R.CLOUD);
					if (dom != null) {
						Element element = dom.getDocumentElement();
						NodeList dcnodList = element
								.getElementsByTagName("datacenter");
						constructCloud(dcnodList);
						NodeList bwnoNodeList = element
								.getElementsByTagName("bandwidth");
						constructBandWidth(bwnoNodeList);

					}
				} catch (FileNotFoundException ex) {
					System.out.println("DDG xml file is not found.");
				}
			} catch (ParserConfigurationException pce) {
				System.out.println("Error while parsing the xml.");
			} catch (SAXException se) {
				System.out.println("Error while parsing the xml.");
			} catch (IOException ioe) {
				System.out.println("Exception while reading the xml.");
			}
		}

		private static void constructData(NodeList dataNodeList) {
			for (int i = 0; i < dataNodeList.getLength(); i++) {
				Element element = (Element) dataNodeList.item(i);
				String dataname = getValueOfTag("name", element);
				String copyno = getValueOfTag("copyno", element);
				DataSet dataSet = dataSets.getDataset(dataname,
						Integer.parseInt(copyno));

				String datasize = getValueOfTag("datasize", element);// ===============================================================================================
				// String datasize = "0";

				dataSet.setDatasize(Double.parseDouble(datasize));
				String gt = getValueOfTag("gt", element);
				dataSet.setGt(Integer.parseInt(gt));
				dataSet.setUsedtasks(getUsedtasks(element));
				dataSet.setCreatetask(tasks.getTask(getValueOfTag("createtask",
						element)));
			}
		}

		private static void constructControl(NodeList nodeList) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				String taskname = getValueOfTag("name", element);
				Task task = tasks.getTask(taskname);
				task.setSuccessors(getNextTasks(element));
			}
		}

		private static void constructCloud(NodeList nodeList) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				String name = getValueOfTag("name", element);
				String cs = getValueOfTag("cs", element);
				DataCenter dataCenter = cloud.getDataCenter(name);
				dataCenter.setCs(Double.parseDouble(cs));
			}
		}

		private static void constructBandWidth(NodeList nodeList) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				String dc1 = getValueOfTag("dc1", element);
				String dc2 = getValueOfTag("dc2", element);
				String bw = getValueOfTag("bw", element);
				cloud.addBandWidth(dc1, dc2, Double.parseDouble(bw));
			}
		}

		/**
		 * Reads the value of the tag with the given tag name and returns it
		 * 
		 * @param tagName
		 * @param datasetElement
		 * @return
		 */
		private static String getValueOfTag(String tagName,
				Element datasetElement) {
			NodeList aNodeList = datasetElement.getElementsByTagName(tagName);
			Element aElement = (Element) aNodeList.item(0);
			if (aElement == null)
				return null;
			String tagValue = aElement.getFirstChild().getNodeValue();
			return tagValue;
		}

		/**
		 * Reads the usedtasks tag of the xml and returns the list of usedtasks
		 * 
		 * @param datasetElement
		 * @return
		 */
		private static ArrayList<Task> getUsedtasks(Element datasetElement) {
			ArrayList<Task> usedtasks = new ArrayList<Task>();

			NodeList usedtasksNodeList = datasetElement
					.getElementsByTagName("usedtasks");
			if (usedtasksNodeList != null && usedtasksNodeList.getLength() > 0) {
				Element usedtasksElement = (Element) usedtasksNodeList.item(0);
				NodeList taskNodeList = usedtasksElement
						.getElementsByTagName("task");
				for (int i = 0; i < taskNodeList.getLength(); i++) {
					Element taskElement = (Element) taskNodeList.item(i);
					String taskName = taskElement.getFirstChild()
							.getNodeValue();
					Task task = tasks.getTask(taskName);
					// Task successorDS = getDataset(successorName);
					usedtasks.add(task);
				}
			}
			return usedtasks;
		}

		/**
		 * Reads the nexttasks tag of the control.xml and returns the list of
		 * nexttasks
		 * 
		 * @param element
		 * @return
		 */
		private static ArrayList<Task> getNextTasks(Element element) {
			// TODO Auto-generated method stub
			ArrayList<Task> nextTasks = new ArrayList<Task>();

			NodeList nexttasksNodeList = element
					.getElementsByTagName("nexttasks");
			if (nexttasksNodeList != null && nexttasksNodeList.getLength() > 0) {
				Element nexttaskseElement = (Element) nexttasksNodeList.item(0);
				NodeList tasknNodeList = nexttaskseElement
						.getElementsByTagName("nexttask");
				for (int i = 0; i < tasknNodeList.getLength(); i++) {
					Element taskElement = (Element) tasknNodeList.item(i);
					String taskName = taskElement.getFirstChild()
							.getNodeValue();
					Task task = tasks.getTask(taskName);
					nextTasks.add(task);
				}
			}
			return nextTasks;
		}

		public static void OutputTheResult(ArrayList<Strategy.S> Ss) {
			// TODO Auto-generated method stub
			int movetimes = 0;
			double transcost = 0;
			double timecost = Double.MAX_VALUE;
			for (strategies.DR.Strategy.S s : Ss)
				if (s.getTimecost() < timecost) {
					timecost = s.getTimecost();
					movetimes = s.getMovetimes();
					transcost = s.getTranscost();
				}
			File file = new File(R.FOLDER + R.outputFolder + "result.txt");
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(
						new FileWriter(file));
				bufferedWriter.write("timecost = ");
				bufferedWriter.write("" + timecost);
				bufferedWriter.newLine();

				bufferedWriter.write("movetimes = ");
				bufferedWriter.write("" + movetimes);
				bufferedWriter.newLine();

				bufferedWriter.write("transcost = ");
				bufferedWriter.write("" + transcost);

				bufferedWriter.flush();
				bufferedWriter.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public static void OutputTheResult(ArrayList<Strategy.S> Ss,
				ArrayList<Strategy.S> Ss2, Strategy.S hadoop) {
			// TODO Auto-generated method stub
			int movetimes = 0;
			double transcost = 0;
			double timecost = Double.MAX_VALUE;
			for (strategies.DR.Strategy.S s : Ss)
				if (s.getTimecost() < timecost) {
					timecost = s.getTimecost();
					movetimes = s.getMovetimes();
					transcost = s.getTranscost();
				}
			File file = new File(R.FOLDER + R.outputFolder + "result.txt");
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(
						new FileWriter(file));
				bufferedWriter.write("无副本策略");
				bufferedWriter.newLine();
				bufferedWriter.write("timecost = ");
				bufferedWriter.write("" + timecost);
				bufferedWriter.newLine();

				bufferedWriter.write("movetimes = ");
				bufferedWriter.write("" + movetimes);
				bufferedWriter.newLine();

				bufferedWriter.write("transcost = ");
				bufferedWriter.write("" + transcost);
				bufferedWriter.newLine();

				movetimes = 0;
				transcost = 0;
				timecost = Double.MAX_VALUE;
				for (strategies.DR.Strategy.S s : Ss2)
					if (s.getTimecost() < timecost) {
						timecost = s.getTimecost();
						movetimes = s.getMovetimes();
						transcost = s.getTranscost();
					}

				bufferedWriter.write("本文策略");
				bufferedWriter.newLine();
				bufferedWriter.write("timecost = ");
				bufferedWriter.write("" + timecost);
				bufferedWriter.newLine();

				bufferedWriter.write("movetimes = ");
				bufferedWriter.write("" + movetimes);
				bufferedWriter.newLine();

				bufferedWriter.write("transcost = ");
				bufferedWriter.write("" + transcost);
				bufferedWriter.newLine();

				bufferedWriter.write("hadoop策略");
				bufferedWriter.newLine();
				bufferedWriter.write("timecost = ");
				bufferedWriter.write("" + hadoop.getTimecost());
				bufferedWriter.newLine();

				bufferedWriter.write("movetimes = ");
				bufferedWriter.write("" + hadoop.getMovetimes());
				bufferedWriter.newLine();

				bufferedWriter.write("transcost = ");
				bufferedWriter.write("" + hadoop.getTranscost());
				bufferedWriter.newLine();

				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class Strategy {
		static Random random = new Random();
		public static int ScCHROMOSOMELENGTH;
		public static int ScGENELENGTH;
		public static int PaCHROMOSOMELENGTH;
		public static int PaGENENLENGTH;
		public static int[] DescripDSC;// 用来描述对应的副本是属于哪个数据集的
		public static ArrayList<S> CH;
		public static ArrayList<Task> tasksequence;

		public static void initialize() {
			{
				ScCHROMOSOMELENGTH = tasks.getTasks().size();
				ScGENELENGTH = dataSets.getDataSets().size();
				PaCHROMOSOMELENGTH = dataSets.getDataSets().size();
				PaGENENLENGTH = (int) (Math.log(cloud.getDataCenters().size())
						/ Math.log(2) + 0.999999);
				DescripDSC = new int[dataSets.getDataSets().size()];
			}
			{
				int pointer = 0;
				for (int i = 0; i < dataSets.getDistinctDataNum(); i++) {
					String name = dataSets.getDataset(i + 1).getName();
					int num = dataSets.gettheCopyNum(name);
					for (int j = 0; j < num; j++)
						DescripDSC[pointer++] = i + 1;
				}
			}
			{
				CH = new ArrayList<S>();
				tasksequence = new ArrayList<Task>();
			}
			{
				ArrayList<Task> tempTasks = new ArrayList<Task>();
				for (Task task : tasks.getTasks())
					tempTasks.add(task);
				while (tempTasks.size() > 0) {
					for (int i = 0; i < tempTasks.size(); i++) {
						// for (Task task : tempTasks) {
						Task task = tempTasks.get(i);
						boolean isSelected = true;
						for (Task Predecessor : task.getPredecessors()) {
							boolean tobeSelected = false;
							for (Task finished : tasksequence) {
								if (finished.getName().equals(
										Predecessor.getName()))
									tobeSelected = true;
							}
							if (!tobeSelected)
								isSelected = false;
						}
						if (isSelected) {
							tasksequence.add(tempTasks.get(i));
							tempTasks.remove(i);
						}
					}
				}
			}
		}

		public static void TimeAndTransAndMoveCosttotal(S s) {
			double cost = 0;
			double transcost = 0;
			int movecost = 0;
			int i = 0;
			Task t = tasksequence.get(i++);
			while (i < tasksequence.size()) {//按照任务执行先后次序遍历出每个任务
				if (false) {// if(t是分支任务)
					// for (每个分支){
					// 记第i个分支对应的子流程为Pi ;
					// 递归计算TimeCosttotal(DC, Pi, s), 结果记为costi;
					// cost = cost + max{costi};
					t = tasksequence.get(i++);
				} else {
					double mincost = Double.MAX_VALUE;
					int tmovecost = 0;
					double ttranscost = 0;
					for (int j = 1; j <= cloud.getDataCenters().size(); j++) {//遍历出每个数据中心
						double sumcost = 0;
						int summovecost = 0;
						double sumtranscost = 0;

						String tdc = cloud.getDataCenter(j).getName();
						int tID = t.getID();
						S.Gene[] Pa = s.getPa();

						for (DataSet dataSet : t.getInputDataSets()) {//遍历出出每个输入数据
							String dName = dataSet.getName();
							double dSize = dataSet.getDatasize();
							int dId = dataSet.getID();
							int copyno = dataSets.gettheCopyNum(dName);

							int pointer = 0;
							while (DescripDSC[pointer] != dId)
								pointer++;

							double mincostDSC = Double.MAX_VALUE;
							for (int k = 0; k < copyno; k++) {//遍历出每个输入数据的副本，找出最“临近”的那一个
								int lc = Pa[pointer + k].getValueofGene();
								String ddc = cloud.getDataCenter(lc).getName();
								if (ddc.equals(tdc)) {
									mincostDSC = 0;
									break;
								}
								double bandwidth = cloud.getBandWidth(tdc, ddc);
								double DSCcost = dSize / bandwidth;
								if (DSCcost < mincostDSC)
									mincostDSC = DSCcost;
							}
							if(mincostDSC==0)
								continue;
							sumcost+=mincostDSC;
							sumtranscost+=dSize;
							summovecost++;
						}
						if (mincost > sumcost) {
							mincost = sumcost;
							tmovecost = summovecost;
							ttranscost = sumtranscost;
						}
					}
					cost += mincost;
					movecost += tmovecost;
					transcost += ttranscost;
					t = tasksequence.get(i++);
				}
			}
			s.setTimecost(cost);
			s.setMovetimes(movecost);
			s.setTranscost(transcost);
		}

		public static ArrayList<S> Heredity() {
			double maxtimecost = 0;
			System.err.println("Create popSize random S");
			int i = 1;
			while (i <= R.popSize) {
				System.err.println("Create " + i + "th random S");
				S s = S.getRandomS();
				if (!CHcontainsS(s)) {
					System.err.println("Create " + i + "th random S Finished");
					CH.add(s);
					i++;
					TimeAndTransAndMoveCosttotal(s);
					if (s.getTimecost() > maxtimecost)
						maxtimecost = s.getTimecost();
				}
			}
			System.err.println("Create popSize random S Finished");

			int curGen = 0;
			while (curGen < R.maxGen) {
				System.out.println("============curGen:" + curGen
						+ "=============");
				printlnLineInfo("变异阶段");
				ArrayList<S> Ss = new ArrayList<S>();
				for (S s : CH) {
					if (random.nextDouble() < R.variation) {
						try {
							s = s.clone();
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// Pa变异阶段
						S.Gene[] Pa = s.getPa();
						while (true) {
							int p = random.nextInt(PaCHROMOSOMELENGTH
									* PaGENENLENGTH) + 1;
							int d = (p - 1) / PaGENENLENGTH;
							int c = (p - 1) % PaGENENLENGTH;
							Pa[d].Reverse(c);
							if (S.isPaValid(Pa))
								break;
							Pa[d].Reverse(c);
						}

						if (!CHcontainsS(s)) {
							Ss.add(s);
							TimeAndTransAndMoveCosttotal(s);
						}
					}
				}

				for (S s : Ss)
					CH.add(s);
				printlnLineInfo("变异阶段->end");

				// 赌轮算法计算概率
				double m = 0;
				for (S s : CH)
					if (m < s.getTimecost())
						m = s.getTimecost();
				m++;// 使结果不至于出现0或者null
				double totaltime = 0;
				for (S s : CH)
					totaltime += m - s.getTimecost();
				double sumtime = 0;
				for (S s : CH) {
					s.setProbilityl(sumtime / totaltime);
					sumtime += m - s.getTimecost();
					s.setProbilityr(sumtime / totaltime);
				}

				System.out.print("交叉阶段");
				int j = 0;
				while (j < R.genSize) {
					double select1 = random.nextDouble();
					double select2 = random.nextDouble();
					S s1 = null, s2 = null;
					for (int k = 0; k < CH.size(); k++) {
						S s = CH.get(k);
						if (s.isSelected(select1) && s.isSelected(select2)) {
							select1 = random.nextDouble();
							select2 = random.nextDouble();
							k = -1;
							continue;
						}
						if (s.isSelected(select1))
							s1 = s;
						if (s.isSelected(select2))
							s2 = s;
					}
					try {
						s1 = s1.clone();
						s2 = s2.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (random.nextDouble() < R.chiasma) {// 交叉操作
						S.Gene[] Pa1 = s1.getPa();
						S.Gene[] Pa2 = s2.getPa();
						
						boolean[] ptemp=new boolean[PaCHROMOSOMELENGTH*PaGENENLENGTH+1];
						while (true) {
							int p = random.nextInt(PaCHROMOSOMELENGTH
									* PaGENENLENGTH) + 1;
							while (true) {
								if(ptemp[p-1]){
									p=random.nextInt(PaCHROMOSOMELENGTH
											* PaGENENLENGTH) + 1;
									continue;
								}
								ptemp[p-1]=true;
								break;
							}
							int d = (p - 1) / PaGENENLENGTH;
							int c = (p - 1) % PaGENENLENGTH;

							for (int k = c; k < PaGENENLENGTH; k++) {
								byte temp = Pa1[d].getBit()[k];
								Pa1[d].getBit()[k] = Pa2[d].getBit()[k];
								Pa2[d].getBit()[k] = temp;
							}
							if (Pa1[d].getValueofGene() > cloud
									.getDataCenters().size()
									|| Pa1[d].getValueofGene() > cloud
											.getDataCenters().size()) {
								for (int k = c; k < PaGENENLENGTH; k++) {
									byte temp = Pa1[d].getBit()[k];
									Pa1[d].getBit()[k] = Pa2[d].getBit()[k];
									Pa2[d].getBit()[k] = temp;
								}
								continue;
							}

							for (int k = d; k < PaCHROMOSOMELENGTH; k++) {
								S.Gene temp = Pa1[k];
								Pa1[k] = Pa2[k];
								Pa2[k] = temp;
							}

							if (S.isPaValid(Pa1) && S.isPaValid(Pa2))
								break;

							for (int k = c; k < PaGENENLENGTH; k++) {
								byte temp = Pa1[d].getBit()[k];
								Pa1[d].getBit()[k] = Pa2[d].getBit()[k];
								Pa2[d].getBit()[k] = temp;
							}

							for (int k = d; k < PaCHROMOSOMELENGTH; k++) {
								S.Gene temp = Pa1[k];
								Pa1[k] = Pa2[k];
								Pa2[k] = temp;
							}

						}
						if (!CHcontainsS(s1)) {
							TimeAndTransAndMoveCosttotal(s1);
							if (s1.getTimecost() < maxtimecost) {
								CH.add(s1);
							}
							j++;
						}
						if (!CHcontainsS(s2)) {
							TimeAndTransAndMoveCosttotal(s2);
							if (s2.getTimecost() < maxtimecost)
								CH.add(s2);
							j++;
						}
					}
				}
				printlnLineInfo("交叉阶段->end");
				// 将CH中各解按其对应的时间开销升序排序
				// 从CH中除去后面genSize个解，
				while (CH.size() > R.popSize) {
					int maxindex = 0;
					maxtimecost = 0;
					for (int k = 0; k < CH.size(); k++) {
						if (CH.get(k).getTimecost() > maxtimecost) {
							maxindex = k;
							maxtimecost = CH.get(k).getTimecost();
						}
					}
					CH.remove(maxindex);
				}
				double mintimecost = Double.MAX_VALUE;
				for (S s : CH)
					if (s.getTimecost() < mintimecost)
						mintimecost = s.getTimecost();
				System.out.println("mintimecost:\t" + mintimecost
						+ "\tmaxtimecost:" + maxtimecost);
				curGen++;
			}
			return CH;
		}

		public static boolean CHcontainsS(S s) {
			for (S stemp : CH) {
				if (s.isTheSame(stemp))
					return true;
			}
			return false;
		}

		private static class S {
			private double timecost = 0;
			private int movetimes = 0;
			private double transcost = 0;
			private double probilityl = 0, probilityr = 0;
			private Gene[] Pa;

			public static S getRandomS() {
				printlnLineInfo("getRandomS");
				S randS = new S();
				printlnLineInfo("Pa");
				// Pa
				Gene[] Pa = new Gene[PaCHROMOSOMELENGTH];
				boolean jumpout = false;
				int dcnum = cloud.getDataCenters().size();
				{
					double[] referstorage = new double[dcnum];
					double[] tempstorage = new double[dcnum];
					for (int i = 0; i < dcnum; i++)
						referstorage[i] = cloud.getDataCenter(i + 1).getCs()
								* R.lamda;
					for (int i = 0; i < PaCHROMOSOMELENGTH; i++)
						Pa[i] = new Gene(PaGENENLENGTH);
					do {
						for (int i = 0; i < dcnum; i++)
							tempstorage[i] = 0;
						jumpout = false;
						int pointer = 0;
						for (int i = 0; i < dataSets.getDistinctDataNum(); i++) {
							DataSet dataSet = dataSets.getDataset(i + 1);
							String name = dataSet.getName();
							double size = dataSet.getDatasize();
							int num = dataSets.gettheCopyNum(name);

							// 随机分配数据中心
							int[] toBePa = new int[num];
							for (int j = 0; j < num; ++j) {
								boolean[] bool = new boolean[dcnum];
								for (int k = 0; k < bool.length; k++)
									bool[k] = false;
								int testcount = 0;
								toBePa[j] = random.nextInt(dcnum) + 1;
								bool[toBePa[j] - 1] = true;
								testcount++;

								int k = 0;
								while (k < j) {
									if (toBePa[k] == toBePa[j]
											|| (referstorage[toBePa[j] - 1] <= tempstorage[toBePa[j] - 1]
													+ size)) {
										toBePa[j] = random.nextInt(dcnum) + 1;

										if (testcount > 2 * dcnum) {
											boolean result = true;
											for (int L = 0; L < bool.length; L++)
												result = result && bool[L];
											if (result) {
												// 现在表示无法为改数据集分配到数据中心，得重来
												jumpout = true;
												break;
											}
										}
										bool[toBePa[j] - 1] = true;
										testcount++;
										k = 0;
									} else {
										k++;
									}
								}
								if (jumpout)
									break;

								tempstorage[toBePa[j] - 1] += size;
								Pa[pointer++].setGene(toBePa[j]);

							}
							if (jumpout)
								break;
							else {
							}
						}
					} while (jumpout || !isPaValid(Pa));
				}
				randS.setPa(Pa);
				return randS;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Object#clone()
			 */
			@Override
			protected S clone() throws CloneNotSupportedException {
				// TODO Auto-generated method stub
				Gene[] newPa = new Gene[Pa.length];
				for (int i = 0; i < Pa.length; i++)
					newPa[i] = Pa[i].clone();
				S s = new S();
				s.setPa(newPa);
				return s;
			}

			/**
			 * @param probilityl
			 *            the probilityl to set
			 */
			public void setProbilityl(double probilityl) {
				this.probilityl = probilityl;
			}

			public boolean isSelected(double rand) {
				return rand >= probilityl && rand < probilityr;
			}

			/**
			 * @param probilityr
			 *            the probilityr to set
			 */
			public void setProbilityr(double probilityr) {
				this.probilityr = probilityr;
			}

			/**
			 * @return the probilityl
			 */
			public double getProbilityl() {
				return probilityl;
			}

			/**
			 * @return the probilityr
			 */
			public double getProbilityr() {
				return probilityr;
			}

			/**
			 * @return the timecost
			 */
			public double getTimecost() {
				return timecost;
			}

			/**
			 * @param timecost
			 *            the timecost to set
			 */
			public void setTimecost(double timecost) {
				this.timecost = timecost;
			}

			/**
			 * @return the movetimes
			 */
			public int getMovetimes() {
				return movetimes;
			}

			/**
			 * @param movetimes
			 *            the movetimes to set
			 */
			public void setMovetimes(int movetimes) {
				this.movetimes = movetimes;
			}

			/**
			 * @return the transcost
			 */
			public double getTranscost() {
				return transcost;
			}

			/**
			 * @param transcost
			 *            the transcost to set
			 */
			public void setTranscost(double transcost) {
				this.transcost = transcost;
			}

			/**
			 * @return the pa
			 */
			public Gene[] getPa() {
				return Pa;
			}

			/**
			 * @param pa
			 *            the pa to set
			 */
			public void setPa(Gene[] pa) {
				Pa = pa;
			}

			public boolean isValid() {
				return isPaValid(Pa);
			}

			public static boolean isPaValid(Gene[] Pa) {
				// Pa第一类无效解
				for (int i = 0; i < Pa.length; i++) {
					if (Pa[i].getValueofGene() > cloud.getDataCenters().size()) {
						return false;
					}
				}
				// Pa第三类无效解
				int pointer = 0;
				for (int i = 0; i < dataSets.getDistinctDataNum(); i++) {
					String name = dataSets.getDataset(i + 1).getName();
					int num = dataSets.gettheCopyNum(name);
					for (int j = 0; j < num; ++j) {
						int k = 0;
						while (k < j) {
							if (Pa[pointer + j].getValueofGene() == Pa[pointer
									+ k].getValueofGene()) {
								return false;
							}
							k++;
						}
					}
					pointer += num;
				}
				// Pa第二类无效解
				int dcnum = cloud.getDataCenters().size();
				double[] stored = new double[dcnum];
				for (int i = 0; i < dcnum; i++)
					stored[i] = 0;
				for (int i = 0; i < Pa.length; i++) {
					stored[Pa[i].getValueofGene() - 1] += dataSets.getDataset(
							DescripDSC[i]).getDatasize();
				}
				for (int i = 0; i < dcnum; ++i)
					if (stored[i] >= (cloud.getDataCenter(i + 1).getCs() * R.lamda)) {
						return false;
					}
				return true;
			}

			public boolean isTheSame(S s) {
				for (int i = 0; i < Pa.length; i++) {
					for (int j = 0; j < Pa[i].getBit().length; j++) {
						if (Pa[i].getBit()[j] != s.getPa()[i].getBit()[j])
							return false;
					}
				}
				return true;
			}

			private static class Gene {
				private byte[] bit;

				Gene() {

				}

				// for Pa
				public void setGene(int i) {
					for (int k = 0; k < bit.length; k++) {
						bit[k] = 0;
					}
					if (i == cloud.getDataCenters().size())
						return;
					int P = 1;
					while (i >= 1) {
						if (i % 2 != 0) {
							Set(bit.length - P);
						} else {
							reSet(bit.length - P);
						}
						i /= 2;
						P++;
					}
				}

				// for Pa
				public int getValueofGene() {

					int re = 0;
					for (int pointer = 1; pointer <= bit.length; pointer++) {
						if (bit[bit.length - pointer] == 1)
							re += Math.pow(2, pointer - 1);
					}
					if (re == 0)
						re = cloud.getDataCenters().size();
					return re;
				}

				public int getState(int i) {
					return bit[i];
				}

				public void Set(int i) {
					bit[i] = 1;
				}

				public void reSet(int i) {
					bit[i] = 0;
				}

				public void Reverse(int i) {
					bit[i] = (byte) (1 - bit[i]);
				}

				Gene(int genelength) {
					bit = new byte[genelength];
					for (int i = 0; i < bit.length; i++)
						bit[i] = 0;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Object#clone()
				 */
				@Override
				protected Gene clone() throws CloneNotSupportedException {
					// TODO Auto-generated method stub
					byte[] newbit = new byte[bit.length];
					for (int i = 0; i < bit.length; i++) {
						newbit[i] = bit[i];
					}
					Gene gene = new Gene();
					gene.setBit(newbit);
					return gene;
				}

				/**
				 * @return the bit
				 */
				public byte[] getBit() {
					return bit;
				}

				/**
				 * @param bit
				 *            the bit to set
				 */
				public void setBit(byte[] bit) {
					this.bit = bit;
				}
			}
		}
	}

	public static int getID() {
		String subs, string = "w123";
		int Ret = 0;
		subs = string.substring(1, string.length());
		try {
			if (subs == null || subs.trim().equals(""))
				subs = Ret + "";
			Ret = Integer.parseInt(subs);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return Ret;
	}

	public static void printlnLineInfo(Object s) {
		System.out.println(new Throwable().getStackTrace()[1] + "\n" + "at:"
				+ new Date().toLocaleString() + "-->" + s);
	}

}
