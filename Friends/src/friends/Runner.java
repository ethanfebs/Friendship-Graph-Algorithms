package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {

	public static void main(String args[]) {
		Graph g;
		try {
			Scanner sc = new Scanner(new File("FriendList.txt"));
			g = new Graph(sc);
			System.out.println(Friends.shortestChain(g, "sam", "sergei"));
			System.out.println(Friends.cliques(g,"penn staTE"));
			System.out.println(Friends.connectors(g));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
}
