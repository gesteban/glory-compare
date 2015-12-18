package glory.comparedick.zkill;

import glory.comparedick.Log;

import java.io.IOException;
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
	private static final String PETITION_NOITEMS_MODIFIERS = "/no-items/";
	private static final String PETITION_MODIFIER_PAGE = "/page/XX/";
	private static final String PETITION_MODIFIER_AFTERKILLID = "/afterKillID/XX/";
	private static final String PETITION_MODIFIER_BEFOREKILLID = "/beforeKillID/XX/";

	private static final long DELAY = 1000;

	private long lastPetitionTimestamp = System.currentTimeMillis();

	public ZkillClient() {
	}

	public String getCharKillsAfterKill(String charId, String killId) {
		waitDelay();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		WebTarget webTarget = target.path(PETITION_CHARACTER_FETCH)
				.path(charId).path(PETITION_NOITEMS_MODIFIERS)
				.path(PETITION_MODIFIER_AFTERKILLID.replace("XX", killId));
		Response response = webTarget.request().get();
		Log.d(TAG, response.toString());
		String responseEntity = null;
		if (response.hasEntity())
			responseEntity = response.readEntity(String.class);
		lastPetitionTimestamp = System.currentTimeMillis();
		return responseEntity;
	}

	public String getCharKillsBeforeKill(String charId, String killId) {
		waitDelay();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		WebTarget webTarget = target.path(PETITION_CHARACTER_FETCH)
				.path(charId).path(PETITION_NOITEMS_MODIFIERS)
				.path(PETITION_MODIFIER_BEFOREKILLID.replace("XX", killId));
		Response response = webTarget.request().get();
		Log.d(TAG, response.toString());
		String responseEntity = null;
		if (response.hasEntity())
			responseEntity = response.readEntity(String.class);
		lastPetitionTimestamp = System.currentTimeMillis();
		return responseEntity;
	}

	public String getCharKillsBetweenKills(String charId, String afterKillId,
			String beforeKillId) {
		waitDelay();
		if (afterKillId == null) {
			return getCharKillsBeforeKill(charId, beforeKillId);
		}
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		WebTarget webTarget = target
				.path(PETITION_CHARACTER_FETCH)
				.path(charId)
				.path(PETITION_NOITEMS_MODIFIERS)
				.path(PETITION_MODIFIER_AFTERKILLID.replace("XX", afterKillId))
				.path(PETITION_MODIFIER_BEFOREKILLID
						.replace("XX", beforeKillId));
		Response response = webTarget.request().get();
		Log.d(TAG, response.toString());
		String responseEntity = null;
		if (response.hasEntity())
			responseEntity = response.readEntity(String.class);
		lastPetitionTimestamp = System.currentTimeMillis();
		return responseEntity;
	}

	public String getLastCharKills(String charId) {
		return getCharKillsAtPage(charId, 1);
	}

	public String getCharKillsAtPage(String charId, int page) {
		waitDelay();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		WebTarget webTarget = target.path(PETITION_CHARACTER_FETCH)
				.path(charId).path(PETITION_NOITEMS_MODIFIERS)
				.path(PETITION_MODIFIER_PAGE.replace("XX", "" + page));
		Response response = webTarget.request().get();
		Log.d(TAG, response.toString());
		String responseEntity = null;
		if (response.hasEntity())
			responseEntity = response.readEntity(String.class);
		lastPetitionTimestamp = System.currentTimeMillis();
		return responseEntity;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(PETITION_BASE).build();
	}

	private void waitDelay() {
		if (lastPetitionTimestamp + DELAY < System.currentTimeMillis()) {
			try {
				Thread.sleep(System.currentTimeMillis() - lastPetitionTimestamp
						- DELAY);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

}
