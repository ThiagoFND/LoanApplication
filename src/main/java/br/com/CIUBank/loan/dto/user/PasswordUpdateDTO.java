package br.com.CIUBank.loan.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PasswordUpdateDTO {
	
    @NotNull(message = "The old password cannot be null")
    @Size(min = 0, message = "The old password must be at least 8 characters long")
    private String oldPassword;

    @NotNull(message = "The new password cannot be null")
    @Size(min = 0, message = "The new password must be at least 8 characters long")
    private String newPassword;

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
}
