import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ArrayListTest {

	public static void main(String[] args) {
		ArrayList<String> liste = new ArrayList<String>();
		
		for(int i=0; i<5; i++)
		{
			liste.add("a"+i);
		}
		
		for(String element : liste)
		{
			System.out.println(element);
		}
		
		
		//----------------------
		
		
		ArrayList<String> allWords = new ArrayList<String>();
		
		try {
			Scanner input = new Scanner(new File("texte"));
			while(input.hasNext())
			{
				String word = input.next();
				allWords.add(word);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String word : allWords)
		{
			System.out.print(word+' ');
		}
		
		System.out.println(' ');
		
		for(int i=(allWords.size()-1);i>0; i--)
		{
			System.out.print(allWords.get(i)+' ');
		}
		
		for(String word : allWords)
		{
			if((word.length()-1) == 's')
			{
				System.out.print((word.toUpperCase()));
			}
			else
			{
				System.out.print(word+' ');				
			}
		}
	}

}
