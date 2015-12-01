package glory.comparedick.test;

import glory.comparedick.zkill.ZkillClient;

public class ZkillClientTest {

	public static void main(String[] args) {

		ZkillClient client = new ZkillClient();

		System.out.println(client.getKillsFromCharacter("90319222"));

	}

}
