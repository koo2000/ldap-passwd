package koo.ldap.ldaputil.webpasswd.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import koo.ldap.ldaputil.webpasswd.service.LdapService;



@Controller
public class ClientFrontendController {

	@Autowired
	private LdapService ldapService;

	@RequestMapping(value="/")
	public String passwordChange(Model model) {

		model.addAttribute("passwordChangeForm", new PasswordChangeForm());
		return "index";
	}


	@PostMapping(value = "/changePassword")
	public String changePasword(
			@Valid @ModelAttribute("passwordChangeForm") PasswordChangeForm passwordChangeForm, 
			BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "index";
		}

		model.addAttribute("passwordChangeForm", new PasswordChangeForm());
		try {
			ldapService.updatePassword(passwordChangeForm.getOldPassword(), passwordChangeForm.getNewPassword());
			model.addAttribute("message", "パスワード変更が成功しました。");
		} catch (BadCredentialsException ex) {
			model.addAttribute("message", "現在のパスワードが違います。");
		}

		return "index";
	}

}
