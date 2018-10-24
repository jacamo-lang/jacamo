import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.junit.Test;

import jacamo.project.JaCaMoProject;
import jacamo.project.parser.JaCaMoProjectParser;
import jacamo.project.parser.ParseException;

/** JUnit test case for syntax package */
public class JaCamoProjectTest {

    JaCaMoProjectParser parser;

    @Test
    public void testToString() {
        boolean ok = true;
        try {
            parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p3.jcm") );
            JaCaMoProject project = parser.parse(".");
            assertEquals("product(\"banana\",\"this is a condition\",15000),rft(gui)",
                    project.getAg("b").getAsSetts(false, false).getUserParameter("beliefs"));
            //System.out.println(project);
            parser = new JaCaMoProjectParser(new StringReader(project.toString()));
            parser.parse(".");
        } catch (Exception e) {
            System.err.println("Error:"+e);
            e.printStackTrace();
            ok = false;
        }
        assertTrue(ok);
    }

    @Test
    public void testParse1() throws FileNotFoundException, ParseException {
        parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p1.jcm") );
        JaCaMoProject project = parser.parse(".");
        System.out.println(project);
    }

    @Test
    public void testParse2() throws FileNotFoundException, ParseException {
        parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p2.jcm") );
        JaCaMoProject project = parser.parse("src/test/java/project");
        System.out.println(project);
    }

    @Test
    public void testParseInst() throws FileNotFoundException, ParseException {
        parser = new JaCaMoProjectParser(new FileReader("src/test/java/project/p4.jcm") );
        JaCaMoProject project = parser.parse("src/test/java/project");
        System.out.println(project);
    }

}
