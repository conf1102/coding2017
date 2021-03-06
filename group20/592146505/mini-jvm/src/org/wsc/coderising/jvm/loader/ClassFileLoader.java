package org.wsc.coderising.jvm.loader;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassFileLoader {

	private List<String> clzPaths = new ArrayList<String>();
	/** 默认缓冲大小 */
	private final static int DEFAULT_SIZE = 32;
	DataInputStream dis;
	private ByteArrayOutputStream baos;

	public byte[] readBinaryCode(String className) throws ClassNotFoundException, IOException {
		byte[] buffer = null;
		File file = null;
		for (String clzPath : clzPaths) {
			clzPath += "/" + className.replace(".", "/") + ".class";
			file = new File(clzPath);
			if (file.exists())
				buffer = getFileToByte(new File(clzPath));
			close();
		}
		if (buffer == null) {
			throw new ClassNotFoundException();
		}
		return buffer;

	}

	public void addClassPath(String path) {
		clzPaths.add(path);
	}

	public String getClassPath() {
		StringBuffer clzPath = new StringBuffer();
		for (int i = 0; i < clzPaths.size(); i++) {
			clzPath.append(clzPaths.get(i));
			if (i < clzPaths.size() - 1)
				clzPath.append(";");
		}
		return clzPath.toString();
	}

	private byte[] getFileToByte(File file) throws IOException {
		byte[] buffer = new byte[DEFAULT_SIZE];
		dis = new DataInputStream(new FileInputStream(file));
		baos = new ByteArrayOutputStream();
		int lenth;
		// 读取
		while ((lenth = dis.read(buffer)) != -1) {
			baos.write(buffer, 0, lenth);
		}
		return baos.toByteArray();
	}

	private void close() {
		try {
			if (dis != null) {
				dis.close();
			}
			if (baos != null) {
				baos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
