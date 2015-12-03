package glory.comparedick.model;

public class Queries {

	public static final String SELECT_CHARACTER_LASTKILLTIME(String charUri) {
		return String.format("PREFIX c: <http://comparedick.glory/def#> "
				+ "SELECT ?killUri WHERE { ?killUri c:killTime ?killTime } "
				+ "ORDER BY DESC(?killTime)", charUri);
	}

}
