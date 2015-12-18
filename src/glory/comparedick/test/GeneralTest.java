package glory.comparedick.test;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import glory.comparedick.Log;
import glory.comparedick.data.KillsModel;

public class GeneralTest {

	// TODO quizás quitar la recuperación de /attackers ?
	// TODO coger losses

	static String charId_Anthony = "90319222";
	static String charId_Peonza = "90480201";
	static String charId_Dax = "94569542";

	static int year = 2015;
	static int month = Calendar.OCTOBER;

	public static void main(String[] args) throws Exception {

		// getPoints();
		createDataForWeb();

	}

	public static void updateSkills() throws Exception {

		KillsModel data = new KillsModel();
		data.loadFromDevice();
		data.updateCharKills(charId_Dax);
		data.saveToDevice();

		Log.i("test", "killCount dax = " + data.getKillCount(charId_Dax));
		Log.i("test", "killCount peonza = " + data.getKillCount(charId_Peonza));

	}

	public static void getPoints() throws Exception {
		KillsModel data = new KillsModel();
		data.loadFromDevice();

		Calendar cal_dec_2015 = Calendar.getInstance();
		cal_dec_2015.set(Calendar.YEAR, 2015);
		cal_dec_2015.set(Calendar.MONTH, Calendar.DECEMBER);
		cal_dec_2015.set(Calendar.DAY_OF_MONTH, 1);
		cal_dec_2015.set(Calendar.HOUR_OF_DAY, 0);
		cal_dec_2015.set(Calendar.MINUTE, 0);
		cal_dec_2015.set(Calendar.SECOND, 0);
		cal_dec_2015.set(Calendar.MILLISECOND, 0);

		Calendar cal_2010 = Calendar.getInstance();
		cal_2010.set(Calendar.YEAR, 2010);
		cal_2010.set(Calendar.MONTH, 1);
		cal_2010.set(Calendar.DAY_OF_MONTH, 1);
		cal_dec_2015.set(Calendar.HOUR_OF_DAY, 0);
		cal_dec_2015.set(Calendar.MINUTE, 0);
		cal_dec_2015.set(Calendar.SECOND, 0);
		cal_dec_2015.set(Calendar.MILLISECOND, 0);

		Calendar cal_now = Calendar.getInstance();

		int points = data.getPoints(charId_Dax, cal_dec_2015.getTime(),
				cal_now.getTime());
		System.out.println("points = " + points);
	}

	public static String createDataForWeb(){

		KillsModel data = new KillsModel();
		data.loadFromDevice();

		JSONArray array = new JSONArray();

		Calendar cal_to = Calendar.getInstance();
		cal_to.set(Calendar.YEAR, year);
		cal_to.set(Calendar.MONTH, month);
		cal_to.set(Calendar.HOUR_OF_DAY, 23);
		cal_to.set(Calendar.MINUTE, 59);
		cal_to.set(Calendar.SECOND, 59);
		Calendar cal_from;
		for (int i = 1; i <= cal_to.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			cal_to.set(Calendar.DAY_OF_MONTH, i);
			cal_from = (Calendar) cal_to.clone();
			cal_from.add(Calendar.DAY_OF_YEAR, -7);
			cal_from.add(Calendar.SECOND, 1);
			int points = data.getPoints(charId_Dax, cal_from.getTime(),
					cal_to.getTime());
			JSONObject object = new JSONObject();
			object.put("x", i);
			object.put("y", points);
			array.put(object);
		}
		Log.i("test", "\n" + array.toString());
		return array.toString();

	}

}
