package glory.comparedick.model;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import glory.comparedick.Log;
import glory.comparedick.Utils;
import glory.comparedick.zkill.ZkillClient;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.JSONArray;

import com.github.jsonldjava.utils.JsonUtils;

public class ZkillModel {

	public static final String TAG = ZkillModel.class.getSimpleName();
	public static final String DATA_PREFIX = "http://comparedick.glory/data/";
	public static final String JSONLD_CONTEXT_FILENAME = "context.json";
	public static final String JSONLD_START = Utils
			.fileToString(JSONLD_CONTEXT_FILENAME);
	public static final String JSONLD_END = "}}";
	public static final String STORE_NAME = "dicks.ttl";

	private ZkillClient _client;
	private Model _model;

	public ZkillModel() {
		_client = new ZkillClient();
		_model = ModelFactory.createDefaultModel();
	}

	public String updateCharKills(String charId) {

		String killUri = "";
		ResultSet rs = doSelect(Queries
				.SELECT_CHARACTER_LASTKILLTIME(uri(charId)));
		if (rs.hasNext()) {
			QuerySolution sol = rs.nextSolution();
			killUri = sol.get("killUri").toString();
		}
		String json = _client.getKillsFromCharacter(charId,
				killUri.replace(DATA_PREFIX, ""));
		String jsonld = createJsonld(charId, json);
		// try {
		// System.out.println(JsonUtils.toPrettyString(JsonUtils
		// .fromString(jsonld)));
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		addJsonld(jsonld);
		return null;
	}

	public int getPoints(String charId, Date from, Date to) {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String points = null;
		ResultSet rs = doSelect(Queries.SELECT_POINTS_BETWEENDATES(uri(charId),
				fmt.format(from), fmt.format(to)));
		if (rs.hasNext()) {
			QuerySolution sol = rs.nextSolution();
			points = sol.get("pointSum").toString();
		}
		try {
			return Integer.parseInt(points.substring(0, points.indexOf("^^")));
		} catch (Exception ex) {
			System.err.println("exception parsing to integer : " + points);
			return -1;
		}
	}

	public boolean loadFromDevice() {
		File aFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator"), STORE_NAME);
		if (aFile.exists()) {
			try {
				RDFDataMgr.read(_model, aFile.getAbsolutePath());
			} catch (org.apache.jena.riot.RiotNotFoundException ex) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean saveToDevice() {
		new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator")).mkdirs();
		File aFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator"), STORE_NAME);
		try {
			RDFDataMgr.write(new FileOutputStream(aFile), _model,
					RDFFormat.TURTLE);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "exception while saving store");
			ex.printStackTrace();
			return false;
		}
	}

	public long size() {
		return _model.size();
	}

	public void print() {
		_model.write(System.out, "TURTLE");
	}

	private ResultSet doSelect(String sparqlQueryString) {
		Log.d(TAG, sparqlQueryString);
		Query query = QueryFactory.create(sparqlQueryString);
		return QueryExecutionFactory.create(query, _model).execSelect();
	}

	private int addJsonld(String jsonld) {
		try {
			RDFDataMgr.read(_model, Utils.convertStringToStream(jsonld),
					Lang.JSONLD);
		} catch (Exception ex) {
			return -1;
		}
		return 1;

	}

	private static String createJsonld(String charId, String jsonKills) {
		JSONArray array = new JSONArray(jsonKills);
		for (int i = 0; i < array.length(); i++) {
			String auxStr, auxStr2;
			// killID int to string
			auxStr = array.getJSONObject(i).get("killID").toString();
			array.getJSONObject(i).put("killID", auxStr);
			// solarSystemID int to string
			auxStr = array.getJSONObject(i).get("solarSystemID").toString();
			array.getJSONObject(i).put("solarSystemID", auxStr);
			// killTime to standard xsd:dateTime
			auxStr = array.getJSONObject(i).get("killTime").toString();
			array.getJSONObject(i).put("killTime", auxStr.replace(" ", "T"));
			// characterID or corporationID int to string + @type + c:name
			auxStr = array.getJSONObject(i).getJSONObject("victim")
					.get("characterID").toString();
			if (auxStr.equals("0")) { // corporation asset kill
				auxStr = array.getJSONObject(i).getJSONObject("victim")
						.get("corporationID").toString();
				array.getJSONObject(i).getJSONObject("victim")
						.put("@type", "c:Corporation");
				auxStr2 = array.getJSONObject(i).getJSONObject("victim")
						.get("corporationName").toString();
			} else {
				array.getJSONObject(i).getJSONObject("victim")
						.put("@type", "c:Character");
				auxStr2 = array.getJSONObject(i).getJSONObject("victim")
						.get("characterName").toString();
			}
			array.getJSONObject(i).getJSONObject("victim").put("@id", auxStr);
			array.getJSONObject(i).getJSONObject("victim")
					.put("c:name", auxStr2);
			// attackers array
			JSONArray array2 = array.getJSONObject(i).getJSONArray("attackers");
			for (int j = 0; j < array2.length(); j++) {
				// characterID or corporationID int to string + @type + c:name
				auxStr = array2.getJSONObject(j).get("characterID").toString();
				if (auxStr.equals("0")) { // corporation asset kill
					auxStr = array2.getJSONObject(j).getJSONObject("attackers")
							.get("corporationID").toString();
					array2.getJSONObject(j).put("@type", "c:Corporation");
					auxStr2 = array2.getJSONObject(j).get("corporationName")
							.toString();
				} else {
					array2.getJSONObject(j).put("@type", "c:Character");
					auxStr2 = array2.getJSONObject(j).get("characterName")
							.toString();
				}
				array2.getJSONObject(j).put("@id", auxStr);
				array2.getJSONObject(j).put("c:name", auxStr2);
			}
			// moving zkb/points and zkb/totalValue elements to root
			double auxFloat = array.getJSONObject(i).getJSONObject("zkb")
					.getDouble("totalValue");
			int auxInt = array.getJSONObject(i).getJSONObject("zkb")
					.getInt("points");
			array.getJSONObject(i).put("c:totalValue", auxFloat);
			array.getJSONObject(i).put("c:points", auxInt);
		}
		return JSONLD_START.replace("CHAR_ID", charId) + array.toString()
				+ JSONLD_END;
	}

	private static String uri(String id) {
		return DATA_PREFIX + id;
	}

}
