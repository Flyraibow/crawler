package crawler.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TxtManager {
	
	public static String readTxtFile(String path)
	{
		return readTxtFile(path,"");
	}
	
	public static String readTxtFile(String path,String returnChar)
	{
		String text = "";
        try {
            String encoding="UTF-8";
            File file=new File(path);
            if(file.isFile() && file.exists())
            {
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	text += lineTxt + returnChar;
                }
                read.close();
            }
            else{
            	System.out.println("cannot find the file");
            }
        } catch (Exception e) {
            System.out.println("error in reading the file");
            e.printStackTrace();
        }
        return text;
    }
	

	public static void writeTxtFile(String str, String path,boolean append) {
		BufferedWriter fw = null;
		try {
			File file = new File(path);
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
			fw.append(str);
			fw.newLine();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
