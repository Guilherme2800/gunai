package com.gunai.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.HtmlUtils;

import com.gunai.model.Greeting;
import com.gunai.model.HelloMessage;
import com.gunai.model.Votation;

@Controller
public class WebSocketController {
	
	private List<String> defaultValues = Arrays.asList(
            "Nestlé",
            "Parmalat",
            "Itambé",
            "Batavo",
            "Piracanjuba",
            "Leitbom",
            "Elegê",
            "Quatá",
            "Jussara",
            "Tirol",
            "Leitinho de Rafa",
            "Leitinho de Daniel",
            "Leitinho de Tonho",
            "Leitinho de Jhonny",
            "Leitinho de TeT"
        );

	private List<Votation> votes = new ArrayList<>();
	
	private List<Votation> defaultVotes = new ArrayList<>();
	
	private Set<String> users = new HashSet<>();
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000);
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}
	
	@MessageMapping("/newRegister")
	@SendTo("/topic/newRegister")
	public Set<String> addUserInSession(String user) throws Exception {
		users.add(user.replaceAll("\"", ""));
		return users;
	}
	
	@MessageMapping("/removeUserInSession")
	@SendTo("/topic/removeUserInSession")
	public Set<String> removeUserInSession(String user) throws Exception {
		users = users.stream().filter(userName -> !userName.equalsIgnoreCase(user)).collect(Collectors.toSet());
		return users;
	}
	
	@MessageMapping("/vote")
	@SendTo("/topic/vote")
	public List<Votation> addVote(Votation votation) throws Exception {
		
		if(votation.getVote() != null) {
			boolean existVote = false;

			for (Votation vote : votes) {
				if (vote.getUser().equalsIgnoreCase(votation.getUser())) {
					vote.setVote(votation.getVote());
					existVote = true;
					continue;
				}
			}
			if (!existVote) {
				votes.add(votation);
				
				Votation vot = new Votation();
				vot.setUser(votation.getUser());
				vot.setVote(defaultValues.get(new Random().nextInt(defaultValues.size())));
				defaultVotes.add(vot);
			}
		}
		
		return defaultVotes;
	}
	
	@MessageMapping("/showVotes")
	@SendTo("/topic/showVotes")
	public List<Votation> showVotes() throws Exception {
		return votes;
	}
	
	@MessageMapping("/cleanVotes")
	@SendTo("/topic/cleanVotes")
	public List<?> cleanVotes() throws Exception {
		votes.clear();
		defaultVotes.clear();
		return new ArrayList<>();
	}
	
	@PutMapping("/cleanUsers")
	public ResponseEntity<?> cleanUsers() throws Exception {
		users.clear();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/")
	public String home() {
		return "index.html";
	}
}
