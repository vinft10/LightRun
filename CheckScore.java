package dot;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CheckScore {
	File highScore = new File("highScore.txt");
	int checkFinal;
	int score;
	public void check(int ticks) throws IOException{
	score = ticks/5;
	if(!highScore.exists())
	{
		highScore.createNewFile();
	}

		
		FileReader read = new FileReader(highScore);
		BufferedReader buffRead = new BufferedReader(read);
		String check = buffRead.readLine();
		
		FileWriter write = new FileWriter(highScore);
		PrintWriter printWrite = new PrintWriter(write);

		int checkParse = Integer.parseInt(check);
 		checkFinal = checkParse;
		read.close();
		buffRead.close();
	
		if( checkFinal < score){
			printWrite.print(score);
		}else{
			printWrite.print(checkFinal);
		}
		write.close();
		printWrite.close();		
	}
}

    
	



