package deprecated;

import java.io.BufferedReader;
import java.io.FileReader;

public class TypeID {

	public static void test() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("pointsIDs.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
				if (line != null && !line.isEmpty() && !line.startsWith("#")) {
					String[] something = line.split("\t");
					System.out.println(something[0] + " - " + something[1]);
				}
			}
		} finally {
			br.close();
		}
	}

}
