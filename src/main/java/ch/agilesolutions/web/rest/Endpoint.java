package ch.agilesolutions.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Endpoint {
	
	@GetMapping(value = "/api")
	public String getVersion() {


		return String.format("You passed authentication");
	}


}
