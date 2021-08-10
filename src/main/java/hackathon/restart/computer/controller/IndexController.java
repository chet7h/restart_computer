package hackathon.restart.computer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hackathon.restart.computer.entity.MyGuests;
import hackathon.restart.computer.service.MyGuestsService;

@Controller
public class IndexController {
	
	@Autowired
	private MyGuestsService myGuestsService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		MyGuests cc = myGuestsService.getInfo().get();
		model.addAttribute("MyGuests", cc);
		return "index";
	}

}
