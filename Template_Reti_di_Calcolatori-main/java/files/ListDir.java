package source.scratch.java.files;

import java.io.*;

public class ListDir {
	public static void main(String[] args) {

		String dirName;

		if (args.length != 1) {
			dirName = ".";
		} else {
			dirName = args[0];
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try {
			if (new File(dirName).exists()) {
				String[] fileList = new File(dirName).list();
				for (int i = 0; i < fileList.length; i++) {
					System.out.println(fileList[i] + " " + fileList[i].length());
				}
			} else {
				System.out.println("Dir non esistente....\n");
			}
		} catch (Exception e) {
			System.out.println("Errore nell'input");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
