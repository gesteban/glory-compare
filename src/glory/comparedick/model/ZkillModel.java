package glory.comparedick.model;

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
import org.json.JSONArray;

public class ZkillModel {
	public static final String DATA_PREFIX = "http://comparedick.glory/data/";
	public static final String JSONLD_CONTEXT_FILENAME = "context.json";
	public static final String JSONLD_START = Utils
			.fileToString(JSONLD_CONTEXT_FILENAME);
	public static final String JSONLD_END = "}}";

	private ZkillClient _client;
	private Model _model;

	public ZkillModel() {
		_client = new ZkillClient();
		_model = ModelFactory.createDefaultModel();
	}

	public String updateCharKills(String charId) {

		String killUri = "";
		System.out.println(Queries
				.SELECT_CHARACTER_LASTKILLTIME(uri(charId)));
		ResultSet rs = doSelect(Queries
				.SELECT_CHARACTER_LASTKILLTIME(uri(charId)));
		if (rs.hasNext()) {
			QuerySolution sol = rs.nextSolution();
			killUri = sol.get("killUri").toString();
		}
		addKillsFromCharacter(charId,
				_client.getKillsFromCharacter(charId, killUri.replace(DATA_PREFIX, "")));
		return null;
	}

	private ResultSet doSelect(String sparqlQueryString) {
		Query query = QueryFactory.create(sparqlQueryString);
		return QueryExecutionFactory.create(query, _model).execSelect();
	}

	private int addKillsFromCharacter(String charId, String jsonKills) {
		try {
			JSONArray array = new JSONArray(jsonKills);
			for (int i = 0; i < array.length(); i++) {
				String aux = array.getJSONObject(i).get("killID").toString();
				array.getJSONObject(i).put("killID", aux);
				aux = array.getJSONObject(i).get("solarSystemID").toString();
				array.getJSONObject(i).put("solarSystemID", aux);
				aux = array.getJSONObject(i).get("killTime").toString();
				array.getJSONObject(i).put("killTime", aux.replace(" ", "T"));
				aux = array.getJSONObject(i).getJSONObject("victim")
						.get("characterID").toString();
				array.getJSONObject(i).getJSONObject("victim")
						.put("characterID", aux);
			}
			RDFDataMgr.read(
					_model,
					Utils.convertStringToStream(JSONLD_START.replace("CHAR_ID",
							charId) + array.toString() + JSONLD_END),
					Lang.JSONLD);
		} catch (Exception ex) {
			return -1;
		}
		return 1;

	}

	private static String uri(String id) {
		return DATA_PREFIX + id;
	}

	public long size() {
		return _model.size();
	}

	public void print() {
		_model.write(System.out, "TURTLE");
	}

}
