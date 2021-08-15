package hackathon.restart.computer.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("control")
public class ControlController {

	@GetMapping({ "/", "" })
	public String index(Model model,Principal principal) {
		return "control";
	}

}
