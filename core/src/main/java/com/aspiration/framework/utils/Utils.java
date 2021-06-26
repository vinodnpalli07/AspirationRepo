package com.aspiration.framework.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Vinod Kumar
 *
 */
public class Utils {

	/**
	 * converts the data present in the file to string
	 *
	 * @param file
	 * @return content of the file
	 */
	public static String getDataFromFile(File file) {
		String result = null;
		DataInputStream in = null;

		try {
			byte[] buffer = new byte[(int) file.length()];
			in = new DataInputStream(new FileInputStream(file));
			in.readFully(buffer);
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("IO problem in fileToString", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// ignore it
			}
		}
		return result;
	}

	public static String getYesterdayDate(String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat(format, locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		return formatter.format(c.getTime());
	}

	public static String getTomoDate(String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat(format, locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		return formatter.format(c.getTime());
	}

	public static String getNextMontDate(String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat(format, locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.add(Calendar.MONTH, 1);
		return formatter.format(c.getTime());
	}

	public static String getNextMontFirstDate(String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat("d,MMM,yyyy", locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, 1);
		Date firstDayOfMonth = c.getTime();
		return formatter.format(firstDayOfMonth);
	}

	/**
	 * @param minutes
	 * @param format expected date to be in this format
	 * @return
	 */
	public static String getTimeAfter(int minutes, String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat(format, locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.add(Calendar.MINUTE, minutes);
		return formatter.format(c.getTime());
	}

	public static String getDateAfter(int daysAfter, String format) {
		Locale locale1 = new Locale("en");
		SimpleDateFormat formatter = new SimpleDateFormat("d,MMM,yyyy,HH,mm,ss", locale1);
		Calendar c = Calendar.getInstance(locale1);
		c.setTime(new Date());
		c.add(Calendar.DATE, daysAfter);
		return formatter.format(c.getTime());
	}

	/**
	 * @param dateStr
	 * @param actualFormat dateStr's current format 
	 * @param expectedFormat expected format of the return str
	 * @return date in expected format
	 */
	public static String getFormattedDate(String dateStr, String actualFormat, String expectedFormat) {
		String returnValue = null;
		try {
			Locale locale1 = new Locale("en");
			Date date = new SimpleDateFormat(actualFormat, locale1).parse(dateStr);
			returnValue = new SimpleDateFormat(expectedFormat, locale1).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * @return current time stamp in a given format
	 *
	 * @param format
	 */
	public static String getCurrentTime(String format) {
		Locale locale1 = new Locale("en");
		return new SimpleDateFormat(format, locale1).format(new Date());
	}

	public static File writeToFile(File file, String content) {
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * converts the xml data which is in string format to actual xml 
	 * 
	 * @param xmlStr
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document convertStrToXML(String xmlStr)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
		return doc;
	}

	/**
	 * encodes the password in "MD5" for a given string
	 * 
	 * @param format ex: "MD5"
	 * @param pswd string to be encoded
	 * @return generated pswd in "MD5" format
	 */
	public static String encodePswd(String format, String pswd) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(format);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		md.update(pswd.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++)
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		return sb.toString();
	}

	 

	/**
	 * extracts a zip file specified by the zipFilePath to a directory specified
	 * by destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public static void unzipFile(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	 
	
	public static String randomStringGen(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String UserName = sb.toString();
		return UserName;
	}
	
	public static String randomMobileNoGen(int startWith, int length) {
		char[] chars = "1234657890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		sb.append(startWith);
		for (int i = 0; i < length-1; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String mobNo = sb.toString();
		return mobNo;
	}
}