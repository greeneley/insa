
package example;

import java.io.IOException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.opencsv.CSVReader;
import org.apache.commons.lang.WordUtils;

public class Hello {
    public static void main(String[] args) {
        String  message = "hello ivy !";
        System.out.println("standard message : " + message);
        System.out.println("capitalized by " + WordUtils.class.getName() 
                                + " : " + WordUtils.capitalizeFully(message));

        int monmax = 0;
        int nb     = 0;
        try
        {
            CSVReader reader = new CSVReader(new FileReader("data.csv"));
            try
            {
                List<String[]> myEntries = reader.readAll();
                Iterator<String[]> iterator = myEntries.iterator();
                iterator.next(); // ligne d'entete du fichier csv

                while (iterator.hasNext()) 
                {
                    String[] entree = iterator.next();
                    int age = Integer.parseInt(entree[3]);
                    if(age>monmax)
                    {
                        monmax = age;
                    }
                    nb++;
                }
                System.out.println("Nombres lus : "+nb);
                System.out.println("Age maximum : "+monmax);
            }
            catch (java.io.IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
        catch (java.io.FileNotFoundException ex)  
        {
            System.out.println(ex.getMessage());
        }
        //CSVReader reader = new CSVReader(new FileReader("data.csv"));
    }
}
        