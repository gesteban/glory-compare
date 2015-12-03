package glory.comparedick.test;

import glory.comparedick.model.ZkillModel;

public class ZkillClientTest {

	public static void main(String[] args) throws Exception {

		ZkillModel model = new ZkillModel();
		model.updateCharKills("90319222");
		model.print();
		model.updateCharKills("90319222");

	}
}
