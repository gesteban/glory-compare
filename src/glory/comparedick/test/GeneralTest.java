package glory.comparedick.test;

import java.util.Calendar;
import java.util.Date;

import deprecated.TypeID;
import glory.comparedick.model.ZkillModel;

public class GeneralTest {

	public static void main(String[] args) throws Exception {

		// one();
		// two();
		three();
	}

	// TODO quizás quitar la recuperación de /attackers ? no parece que se
	// necesite

	public static void one() throws Exception {
		ZkillModel model = new ZkillModel();
		model.loadFromDevice();
		model.updateCharKills("90319222"); // Anthony Blackwater
		// TODO coger datos más allá de las primera página (>200 kills)
		// TODO coger losses
		// model.updateCharKills("90480201"); // Peonza Chan
		model.saveToDevice();
	}

	public static void two() throws Exception {
		TypeID.test();
	}

	public static void three() throws Exception {
		ZkillModel model = new ZkillModel();
		model.loadFromDevice();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2010);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date date = cal.getTime();

		int points = model.getPoints("90319222", date,
				new Date(System.currentTimeMillis()));
		System.out.println("points = " + points);
	}

}
