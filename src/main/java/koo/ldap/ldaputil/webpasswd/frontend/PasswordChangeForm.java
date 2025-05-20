package koo.ldap.ldaputil.webpasswd.frontend;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public class PasswordChangeForm {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }
    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    @AssertTrue(message = "パスワードが一致していません")
    public boolean isSamePasword(){
        if (newPassword == null || newPasswordConfirm == null) {
            return true;
        } else {
            return newPassword.equals(newPasswordConfirm);
        }
    }
}
