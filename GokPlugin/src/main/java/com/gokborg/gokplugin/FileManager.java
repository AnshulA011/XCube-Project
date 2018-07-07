package com.gokborg.gokplugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {
	private static Object newObject;

	public static void read(String fileName) throws IOException, ClassNotFoundException {

		new Thread() {
			public void run() {
				try {
					FileInputStream is = new FileInputStream(fileName);
					BufferedInputStream bis = new BufferedInputStream(is);
					ObjectInputStream ois = new ObjectInputStream(bis);
					newObject = ois.readObject();
					ois.close();
					bis.close();
					is.close();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();

	}
	
	public static boolean fileExists(String fileName) {
		File f = new File(fileName);
		if(f.exists()) {
			return true;
		}
		else {
			return false;
		}
	}

	public static Object getReadObject() {
		return newObject;
	}

	public static void write(String fileName, Object object) throws IOException {
		new Thread() {
			public void run() {
				try {
					FileOutputStream os = new FileOutputStream(fileName);
					BufferedOutputStream bos = new BufferedOutputStream(os);
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					oos.writeObject(object);
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

}
