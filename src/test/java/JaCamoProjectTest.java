import jacamo.infra.RunJaCaMoProject;
import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.project.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import junit.framework.TestCase;

/** JUnit test case for syntax package */
public class JaCamoProjectTest extends TestCase {

    JaCaMoProjectParser parser;
     
    public void testToString() {
        boolean ok = true;
        try {
            parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p3.jcm") );
            JaCaMoProject project = parser.parse(".");
            assertEquals("product(\"banana\",\"this is a condition\",15000),rft(gui)", 
                    project.getAg("b").getAsSetts(false, false).getUserParameter("beliefs"));
            System.out.println(project);
            parser = new JaCaMoProjectParser(new StringReader(project.toString()));
            parser.parse(".");
        } catch (Exception e) {
            System.err.println("Error:"+e);
            e.printStackTrace();
            ok = false;
        }
        assertTrue(ok);
    }
    
    public void testParse1() throws FileNotFoundException, ParseException {
        parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p1.jcm") );
        JaCaMoProject project = parser.parse(".");
        System.out.println(project);
    }

    public void testParse2() throws FileNotFoundException, ParseException {
        parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p2.jcm") );
        JaCaMoProject project = parser.parse("src/test/java/project");
        System.out.println(project);
    }

    
    public void testWriteScript() {
        RunJaCaMoProject.main(new String[] { "src/test/java/project/p1.jcm","run"}); // change to run to test if it runs
    }

}
