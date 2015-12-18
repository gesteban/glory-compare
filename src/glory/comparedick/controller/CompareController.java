package glory.comparedick.controller;

import glory.comparedick.test.GeneralTest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CompareController {

	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {

		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
	@RequestMapping("glory-test")
	public ModelAndView helloWorld2() {
		
		return new ModelAndView("glory-test", "data", GeneralTest.createDataForWeb());
	}
	
}