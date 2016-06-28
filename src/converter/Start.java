package converter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import converter.Field.AligmentField;
import converter.Format.Type;

public class Start {

	public static void main(String[] args) {
		String path = "";
		try {
			path = new File(".").getCanonicalPath();
			Source source = new Source(path+"\\other\\", "1908003z.xml");
			Maket maket= new Maket(new File(path + "\\other\\maket_default.xml"),source);
			maket.toString();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = "";
	}

}
