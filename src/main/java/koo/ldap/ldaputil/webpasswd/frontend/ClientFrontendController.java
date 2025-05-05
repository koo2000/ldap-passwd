package koo.ldap.ldaputil.webpasswd.frontend;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class ClientFrontendController {

	@RequestMapping("/")
	public String open(Model model) {
		String str = "Hello World";
		model.addAttribute("value", str);
		return "index";
	}
}
