package koo.ldap.ldaputil.webpasswd.frontend;

import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientFrontendController {

	@RequestMapping("/")
	public String open(Authentication authentication, Model model) {

		String roles = authentication.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(","));
;
		
        model.addAttribute("role", roles);

		return "index";
	}
}
