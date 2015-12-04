package glory.comparedick.model;

public class Queries {

	public static final String SELECT_CHARACTER_LASTKILLTIME(String charUri) {
		return String
				.format("PREFIX c: <http://comparedick.glory/def#> "
						+ "SELECT ?killUri "
						+ "WHERE { <%s> ?kill ?killUri "
						+ ". ?killUri c:killTime ?killTime "
						+ "} "
						+ "ORDER BY DESC(?killTime) "
						, charUri);
	}

	public static final String SELECT_POINTS_BETWEENDATES(String charUri, String from, String to) {
		return String
				.format("PREFIX c: <http://comparedick.glory/def#> "
						+ "PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#> "
						+ "SELECT (SUM(?points) AS ?pointSum) "
						+ "WHERE { <%s> c:kill ?killUri "
						+ ". ?killUri c:killTime ?killTime "
						+ ". ?killUri c:points ?points . "
						+ "FILTER ( ?killTime > \"%s\"^^xsd:dateTime ) "
						+ "FILTER ( ?killTime < \"%s\"^^xsd:dateTime ) "
						+ "} "
						, charUri, from, to);
	}

}
