package com.web.FileExplorer.Controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.FileExplorer.dto.ViewingObject;
import com.web.FileExplorer.dto.ViewingObjectHolder;
import com.web.FileExplorer.services.DirectoryTraverserService;

@Controller
public class MainController {
	// TODO set up way for jar to have authentication and user files outside of jar
	@Autowired
	private HashMap<String, ViewingObjectHolder> holders = null;

	@GetMapping(value = "/")
	public String home(Model model, @AuthenticationPrincipal User user) {
		// TODO homescreen
		String username = "base";

		if (user != null) {
			username = user.getUsername();
		}

		model.addAttribute("objects", holders.get(username).getMutables());
		model.addAttribute("title", "Currently available files:");

		return "main";
	}

	@PostMapping("/filesetting")
	public String fileSettings(Model model, @AuthenticationPrincipal User user,
			@RequestParam(value = "location", required = true) String pathID,
			@RequestParam(value = "newTags", required = false) String tags,
			@RequestParam(value = "newTitle", required = false) String newTitle) {
		String username = "base";

		if (user != null) {
			username = user.getUsername();
		}

		ViewingObjectHolder holder = holders.get(username);
		ViewingObject target = holder.getByLocation(pathID);

		if (tags != null && !tags.isEmpty()) {
			System.out.println("tags:" + tags);
			String[] temp = tags.toLowerCase().split(",");
			ArrayList<String> list = new ArrayList<>();
			for (String s : temp) {
				list.add(s.strip());
			}
			holder.addTagToObject(pathID, list);
		}

		if (newTitle != null && !newTitle.isBlank()) {
			target.setName(newTitle);
		}

		model.addAttribute("mainView", target);

		return "fileSettings";
	}

	@PostMapping("/removefile/**")
	public String remove(Model model, @AuthenticationPrincipal User user, @RequestParam("location") String pathID) {
		String username = "base";

		if (user != null) {
			username = user.getUsername();
		}

		holders.get(username).removeObject(pathID);

		return "redirect:/";
	}

	@RequestMapping("/help/**")
	public String getHelp(Model model) {
		return "help";
	}

	@PostMapping(value = "/seed")
	public String seed(Model model, @AuthenticationPrincipal User user,
			@RequestParam(value = "path", required = true) String path) {
		// TODO seed the storage with files
		String username = "base";

		if (user != null) {
			username = user.getUsername();
		}

		if (path == null) {
			System.err.println("\n\nNULL NULL NULL\n\n");
			System.exit(9);
		}

		if (path != null && !path.isEmpty()) {
			DirectoryTraverserService.traverseDirectory(Paths.get(path), holders.get(username));
		}

		model.addAttribute("objects", holders.get(username).getMutables());
		return "redirect:/";
	}

	@RequestMapping(value = "/view/**", method = RequestMethod.GET)
	public String view(ModelMap model, HttpServletRequest request, @AuthenticationPrincipal User user) {
		// TODO this is for simply viewing the vids
		String username = "base";
		String url = null;

		if (user != null) {
			username = user.getUsername();
		}

		try {
			url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String pathID = url.substring(url.indexOf("/view") + 5);
		ViewingObjectHolder holder = new ViewingObjectHolder();
		holder.addObjects(holders.get(username).getMutables());
		ViewingObject target = holder.removeObject(pathID);

		if (target == null) {
			// TODO just to keep it from crashing
			return "redirect:/";
		}

		model.addAttribute("mainView", target);
		model.addAttribute("objects", holder.getMutables());

		return "view";
	}

	@RequestMapping(value = "/view/**", method = RequestMethod.POST)
	public String viewPost(ModelMap model, HttpServletRequest request, @AuthenticationPrincipal User user,
			@RequestParam(value = "location", required = true) String pathID,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "secure", required = false) boolean secure) {
		// this is for adding tags
		String username = "base";

		if (user != null) {
			username = user.getUsername();
		}

		if (secure) {
			// want to be secure
			return view(model, request, user);
		}

		if (pathID != null && tags != null) {
			List<String> tagList = Arrays.asList(tags.split(","));
			holders.get(username).addTagToObject(pathID, tagList);
		}

		return "redirect:/view" + pathID;
	}

	@RequestMapping(value = "/search/**", method = RequestMethod.GET)
	public String search(Model model, HttpServletRequest httpRequest, @AuthenticationPrincipal User user) {
		// TODO
		String username = "base";
		String query = httpRequest.getQueryString().substring("query=".length());

		if (user != null) {
			username = user.getUsername();
		}

		model.addAttribute("objects", holders.get(username).search(query));
		model.addAttribute("title", "Search Results:");

		return "main";
	}

}
