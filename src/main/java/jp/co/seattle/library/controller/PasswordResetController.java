package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.UsersService;

@Controller
public class PasswordResetController {
	final static Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping(value = "passwordReset", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	public String resetPassword(Model model) {
		return "passwordReset";
	}
	/**
	 * パスワードリセット
	 *
	 * @param email            メールアドレス
	 * @param password         パスワード
	 * @param passwordForCheck 確認用パスワード
	 * @param model
	 * @return ログイン画面に遷移
	 */
	@Transactional
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPassword(Locale locale, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("passwordForCheck") String passwordForCheck,
			Model model) {

				// バリデーションチェック、パスワード一致チェック
				if (password.length() >= 8 && password.matches("^[A-Za-z0-9]+$")) {
					if (password.equals(passwordForCheck)) {
						// パラメータで受け取ったアカウント情報をDtoに格納する。
						UserInfo userInfo = new UserInfo();
						userInfo.setEmail(email);
						userInfo.setPassword(password);
						usersService.registUser(userInfo);
						return "redirect:/login";
					} else {
						model.addAttribute("errorMessage", "パスワードが一致しません");
						return "passwordReset";
					}
				} else {
					model.addAttribute("errorMessage", "パスワードは8文字以上かつ半角英数字に設定してください");
					return "passwordReset";
				}		
		
	}
}
