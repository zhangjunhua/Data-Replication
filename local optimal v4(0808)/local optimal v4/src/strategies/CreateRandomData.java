package strategies;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Random;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * ���ڹ�������Ĳ������ݣ���Щ����Ĳ���������xml�ļ�����ʽ�����ļ�����
 * 
 * ��ѧ��������Ϊ�������ݺͿ���������������������Ĳ�ͬ�Ա��������еĽ��û��ʵ���Ե�Ӱ�죬
 * Ŀǰ��ʱ�������Կ������̣��Ժ���ʱ�佫��Ľ�Ϊ���ӷ���ʵ����������̣�����������������ͼ����ʽ���������ļ����С�
 * 
 * Ϊ�����ɺ���Ĳ������ݣ��������ݵ�����˳��Ϊ���������ļ�����->���ݼ�������->���ݼ��������ϡ������Ż���
 */




public class CreateRandomData {
	static Random random = new Random();
	public static Scanner scanner;

	public static void newfolderandrstconf() {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					new File(R.FOLDER + R.CONFIGURATION)));
			for (Entry<String, String> entry : R.configerationhMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				value = value.substring(0, value.length() - 1)
						+ (Integer
								.parseInt(value.substring(value.length() - 1)) + 1);
				R.configerationhMap.put(key, value);
				bufferedWriter.write(key + "\t " + value + "\n");
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		R.inputFolder = R.configerationhMap.get("inputdatafolder") + "/";
		R.outputFolder = R.configerationhMap.get("outputdatafolder") + "/";
		File file = new File(R.FOLDER + R.inputFolder);
		file.mkdirs();
		file = new File(R.FOLDER + R.outputFolder);
		file.mkdirs();
	}

	public static void createData() throws IOException {
		double totalstorage = 0;
		double totalsize = 0;

		// �����������ļ�����
		DR.printlnLineInfo("�����������ļ�����");
		final int DCnum = randomint(R.minDCnum, R.maxDCnum);
		DC[] dcs = new DC[DCnum];
		for (int i = 0; i < DCnum; i++) {
			dcs[i] = new DC();
			dcs[i].name = "c" + (i + 1);
			dcs[i].storage = randomdouble(R.minDCstorage, R.maxDCstorage);
			totalstorage += dcs[i].storage;
		}
		double[] bandWidthes = new double[DCnum * (DCnum - 1) / 2];
		for (int i = 0; i < bandWidthes.length; i++) {
			bandWidthes[i] = randomdouble(R.minBandWith, R.maxBandWidth);
		}
		// ������ʼ���ݼ�������
		DR.printlnLineInfo("������ʼ���ݼ�������");
		final int DSnum = randomint(R.miniDSnum, R.maxiDSnum);
		final int Tnum = randomint(R.minTnum, R.maxTnum);
		DS[] dss = new DS[DSnum + Tnum];
		for (int i = 0; i < DSnum + Tnum; i++) {
			dss[i] = new DS();
			dss[i].name = "d" + (i + 1);
			if (i < DSnum) {
				dss[i].gt = 0;
			} else {
				dss[i].gt = 1;
			}
		}
		for (int i = 0; i < DSnum + Tnum; i++) {
			dss[i].size = randomdouble(R.minDsSize, R.maxDsSize);
			if (i < DSnum)
				dss[i].copyno = R.maxCopyno > 0 ? randomint(R.minCopyno,
						R.maxCopyno) : randomint(1, 5);
			totalsize += dss[i].size * dss[i].copyno;
			if (totalsize > totalstorage * R.lamda) {
				totalsize = 0;
				i = -1;
			}
		}
		T[] ts = new T[Tnum];
		for (int i = 0; i < Tnum; i++) {
			ts[i] = new T();
			ts[i].name = "t" + (i + 1);
			dss[i + DSnum].createtask = ts[i].name;
		}
		for (int i = 0; i < Tnum - 1; i++) {
			dss[i + DSnum].usedtasks.add(ts[i + 1].name);
			ts[i].successor = "t" + (i + 2);
		}

		for (int i = 0; i < Tnum; i++) {
			ts[i].needed = randomint(R.minIDS, R.maxIDS);
			for (int j = 0; j < ts[i].needed; j++) {
				int randd = randomint(0, DSnum + i - 2);
				if (!dss[randd].usedtasks.contains(ts[i].name)) {
					dss[randd].usedtasks.add(ts[i].name);
				}
			}
		}
		// ==========================================================================================================================
		// �������õ������������д���ļ�
		DR.printlnLineInfo("�������õ������������д���ļ�");
		// �������ĺ�����
		{
			Document cloudDocument = DocumentHelper.createDocument();
			Element rootCloudElement = cloudDocument.addElement("cloud");
			for (int i = 0; i < DCnum; i++) {
				Element datacenterElement = rootCloudElement
						.addElement("datacenter");
				datacenterElement.addElement("name").setText(dcs[i].name);
				datacenterElement.addElement("cs").setText("" + dcs[i].storage);
			}
			int pointer = 0;
			for (int i = 0; i < DCnum - 1; i++) {
				for (int j = i + 1; j < DCnum; j++) {
					Element bandwidthElement = rootCloudElement
							.addElement("bandwidth");
					bandwidthElement.addElement("dc1").setText(dcs[i].name);
					bandwidthElement.addElement("dc2").setText(dcs[j].name);
					bandwidthElement.addElement("bw").setText(
							"" + bandWidthes[pointer++]);
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(new File(R.FOLDER
					+ R.inputFolder + R.CLOUD)), format);
			writer.write(cloudDocument);
			writer.close();
		}
		// ���и��������ݼ�
		{
			Document datasetsDocument = DocumentHelper.createDocument();
			Element rootElement = datasetsDocument.addElement("datasets");
			for (int i = 0; i < DSnum; i++) {
				for (int j = 0; j < dss[i].copyno; j++) {
					Element datasetElement = rootElement.addElement("dataset");
					datasetElement.addElement("name").setText(dss[i].name);
					datasetElement.addElement("copyno").setText("" + (j + 1));
					datasetElement.addElement("datasize").setText(
							"" + dss[i].size);
					datasetElement.addElement("gt").setText("" + dss[i].gt);
					if (dss[i].createtask != null)
						datasetElement.addElement("createtask").setText(
								dss[i].createtask);
					if (j == 0) {
						Element usedtasksElement = datasetElement
								.addElement("usedtasks");
						for (String t : dss[i].usedtasks)
							usedtasksElement.addElement("task").setText(t);
					}
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(new File(R.FOLDER
					+ R.inputFolder + R.DATASETS)), format);
			writer.write(datasetsDocument);
			writer.close();
		}

		// ����
		{
			Document controlDocument = DocumentHelper.createDocument();
			Element rootElement = controlDocument.addElement("tasks");
			for (int i = 0; i < Tnum; i++) {
				Element taskElement = rootElement.addElement("task");
				taskElement.addElement("name").setText(ts[i].name);
				if (ts[i].successor != null)
					taskElement.addElement("nexttasks").addElement("nexttask")
							.setText(ts[i].successor);
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(new File(R.FOLDER
					+ R.inputFolder + R.CONTROL)), format);
			writer.write(controlDocument);
			writer.close();
		}
	}

	// ��¼������Ϣ
	public static void writeArgs() {
		File file = new File(R.FOLDER + R.inputFolder + "args.txt");
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					file));
			bufferedWriter.write("maxDCnum = ");
			bufferedWriter.write("" + R.maxDCnum);
			bufferedWriter.newLine();

			bufferedWriter.write("minDCnum = ");
			bufferedWriter.write("" + R.minDCnum);
			bufferedWriter.newLine();

			bufferedWriter.write("maxDCstorage = ");
			bufferedWriter.write("" + R.maxDCstorage);
			bufferedWriter.newLine();

			bufferedWriter.write("minDCstorage = ");
			bufferedWriter.write("" + R.minDCstorage);
			bufferedWriter.newLine();

			bufferedWriter.write("maxBandWidth = ");
			bufferedWriter.write("" + R.maxBandWidth);
			bufferedWriter.newLine();

			bufferedWriter.write("minBandWith = ");
			bufferedWriter.write("" + R.minBandWith);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("maxDSnum = ");
			bufferedWriter.write("" + R.maxiDSnum);
			bufferedWriter.newLine();

			bufferedWriter.write("minDSnum = ");
			bufferedWriter.write("" + R.miniDSnum);
			bufferedWriter.newLine();

			bufferedWriter.write("maxDsSize = ");
			bufferedWriter.write("" + R.maxDsSize);
			bufferedWriter.newLine();

			bufferedWriter.write("minDsSize = ");
			bufferedWriter.write("" + R.minDsSize);
			bufferedWriter.newLine();

			bufferedWriter.write("maxCopyno = ");
			bufferedWriter.write("" + R.maxCopyno);
			bufferedWriter.newLine();

			bufferedWriter.write("minCopyno = ");
			bufferedWriter.write("" + R.minCopyno);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("maxTnum = ");
			bufferedWriter.write("" + R.maxTnum);
			bufferedWriter.newLine();

			bufferedWriter.write("minTnum = ");
			bufferedWriter.write("" + R.minTnum);
			bufferedWriter.newLine();

			bufferedWriter.write("maxIDS = ");
			bufferedWriter.write("" + R.maxIDS);
			bufferedWriter.newLine();

			bufferedWriter.write("minIDS = ");
			bufferedWriter.write("" + R.minIDS);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("lamda = ");
			bufferedWriter.write("" + R.lamda);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("variation = ");
			bufferedWriter.write("" + R.variation);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("chiasma = ");
			bufferedWriter.write("" + R.chiasma);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("genSize = ");
			bufferedWriter.write("" + R.genSize);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("popSize = ");
			bufferedWriter.write("" + R.popSize);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.write("maxGen = ");
			bufferedWriter.write("" + R.maxGen);
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int randomint(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static double randomdouble(double min, double max) {
		return min + random.nextDouble() * (max - min);
	}

	private static class DC {
		String name;
		double storage;
	}

	private static class T {
		String name;
		String successor;
		int needed;
	}

	private static class DS {
		String name;
		int copyno;
		ArrayList<String> usedtasks = new ArrayList<String>();
		String createtask;
		double size;
		int gt;
	}
}