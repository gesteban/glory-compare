package glory.comparedick.data;

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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.JSONArray;
import org.json.JSONException;

// TODO implement this using Jena TDB
public class KillsModel {

	public static final String TAG = KillsModel.class.getSimpleName();
	public static final String DATA_PREFIX = "http://comparedick.glory/data/";
	public static final String JSONLD_CONTEXT_FILENAME = "context.json";
	public static final String JSONLD_START = Utils
			.fileToString(JSONLD_CONTEXT_FILENAME);
	public static final String JSONLD_END = "}}";
	public static final String STORE_NAME = "dicks.ttl";

	private ZkillClient _client;
	private Model _model;

	public KillsModel() {
		_client = new ZkillClient();
		_model = ModelFactory.createDefaultModel();
	}

	public void updateCharKills(String charId) {
		ResultSet rs = doSelect(Queries.SELECT_LASTKILLID(asUri(charId)),
				_model);
		String json;
		if (rs.hasNext()) {
			// It has at least one kill.
			// Retrieving kills made after the last stored one.
			String killUri = rs.nextSolution().get("killUri").toString();
			json = _client.getCharKillsAfterKill(charId, asId(killUri));
		} else {
			// It has no kills.
			// Retriving last kills.
			json = _client.getLastCharKills(charId);
		}
		ResultSet rs_olds = doSelect(Queries.SELECT_LASTKILLID(asUri(charId)),
				_model);
		String newest_of_olds = rs_olds.hasNext() ? rs_olds.nextSolution()
				.get("killUri").toString() : null;
		Model resultModel = asModel(createJsonld(charId, json));
		Log.d(TAG, "added " + getKillCount(charId, resultModel)
				+ " kills to the model");
		while (getKillCount(charId, resultModel) >= 200) {
			// Last kills count may be more than 200.
			// We have to retrieve next kills.
			ResultSet rs_news = doSelect(
					Queries.SELECT_OLDESTKILLID(asUri(charId)), resultModel);
			String oldest_of_news = rs_news.nextSolution().get("killUri")
					.toString();

			String json2 = _client.getCharKillsBetweenKills(charId,
					asId(newest_of_olds), asId(oldest_of_news));
			_model.add(resultModel);
			resultModel = asModel(createJsonld(charId, json2));
			Log.d(TAG, "added " + getKillCount(charId, resultModel)
					+ " kills to the model");
		}
		_model.add(resultModel);
	}

	public int getPoints(String charId, Date from, Date to) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String points = null;
		ResultSet rs = doSelect(
				Queries.SELECT_POINTS(asUri(charId), fmt.format(from),
						fmt.format(to)), _model);
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

	public int getKillCount(String charId) {
		return getKillCount(charId, _model);
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

	private static int getKillCount(String charId, Model model) {
		String killCount = null;
		ResultSet rs = doSelect(Queries.SELECT_KILLCOUNT(asUri(charId)), model);
		if (rs.hasNext()) {
			QuerySolution sol = rs.nextSolution();
			killCount = sol.get("killCount").toString();
		}
		try {
			return Integer.parseInt(killCount.substring(0,
					killCount.indexOf("^^")));
		} catch (Exception ex) {
			Log.e(TAG, "exception parsing to integer : " + killCount);
			return -1;
		}
	}
	
	public ResultSet doSelect(String sparqlQueryString) {
		Log.d(TAG, sparqlQueryString);
		Query query = QueryFactory.create(sparqlQueryString);
		return QueryExecutionFactory.create(query, _model).execSelect();
	}

	private static ResultSet doSelect(String sparqlQueryString, Model model) {
		Log.d(TAG, sparqlQueryString);
		Query query = QueryFactory.create(sparqlQueryString);
		return QueryExecutionFactory.create(query, model).execSelect();
	}

	private static Model asModel(String jsonld) {
		Model model = ModelFactory.createDefaultModel();
		try {
			RDFDataMgr.read(model, Utils.convertStringToStream(jsonld),
					Lang.JSONLD);
		} catch (Exception ex) {
			return null;
		}
		return model;

	}

	private static String createJsonld(String charId, String jsonKills) {
		String killId = "";
		try {
			JSONArray array = new JSONArray(jsonKills);
			for (int i = 0; i < array.length(); i++) {
				String auxStr, auxStr2;
				// killID int to string
				auxStr = array.getJSONObject(i).get("killID").toString();
				killId = auxStr;
				array.getJSONObject(i).put("killID", auxStr);
				// solarSystemID int to string
				auxStr = array.getJSONObject(i).get("solarSystemID").toString();
				array.getJSONObject(i).put("solarSystemID", auxStr);
				// killTime to standard xsd:dateTime
				auxStr = array.getJSONObject(i).get("killTime").toString();
				array.getJSONObject(i)
						.put("killTime", auxStr.replace(" ", "T"));
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
				array.getJSONObject(i).getJSONObject("victim")
						.put("@id", auxStr);
				array.getJSONObject(i).getJSONObject("victim")
						.put("c:name", auxStr2);
				// attackers array
				JSONArray array2 = array.getJSONObject(i).getJSONArray(
						"attackers");
				for (int j = 0; j < array2.length(); j++) {
					// characterID or corporationID int to string + @type +
					// c:name
					auxStr = array2.getJSONObject(j).get("characterID")
							.toString();
					if (auxStr.equals("0")) { // corporation asset kill
						auxStr = array2.getJSONObject(j).get("corporationID")
								.toString();
						array2.getJSONObject(j).put("@type", "c:Corporation");
						auxStr2 = array2.getJSONObject(j)
								.get("corporationName").toString();
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
		} catch (JSONException ex) {
			Log.e(TAG, "exception while parsing killId = " + killId);
			ex.printStackTrace();
			return null;
		}
	}

	private static String asUri(String id) {
		return DATA_PREFIX + id;
	}

	private static String asId(String uri) {
		return uri == null ? null : uri.replace(DATA_PREFIX, "");
	}

}
