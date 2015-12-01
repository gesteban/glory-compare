package glory.comparedick.zkill;

import glory.comparedick.Log;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class ZkillClient {

	public static final String TAG = ZkillClient.class.getSimpleName();

	private static final String PETITION_BASE = "https://zkillboard.com/api/kills/";
	private static final String PETITION_CHARACTER_FETCH = "/characterID/";
	private static final String PETITION_CORPORATION_FETCH = "/corporationID/";
	private static final String PETITION_ALLIANCE_FETCH = "/allianceID/";
	private static final String PETITION_INFORMATION_MODIFIERS = "/no-items/no-attackers/";

	public ZkillClient() {

	}

	public String getKillsFromCharacter(String charId) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		Response response = target.path(PETITION_CHARACTER_FETCH).path(charId)
				.path(PETITION_INFORMATION_MODIFIERS).request().get();
		Log.d(TAG, response.toString());
		String responseEntity = null;
		if (response.hasEntity())
			responseEntity = response.readEntity(String.class);
		return responseEntity;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(PETITION_BASE).build();
	}

}
