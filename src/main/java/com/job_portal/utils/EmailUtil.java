package com.job_portal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendOtpEmail(String email, String otp) throws MessagingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

	    mimeMessageHelper.setTo(email);
	    mimeMessageHelper.setSubject("Xác nhận mã OTP");
	    mimeMessageHelper.setText("""
	            <div style="font-family: Arial, sans-serif; line-height: 1.5;">
	                <p>Chào bạn,</p>
	                <p>Bạn đã yêu cầu xác nhận tài khoản. Đây là mã OTP của bạn:</p>
	                <h2 style="color: #2E86C1;">%s</h2>
	                <p>Vui lòng nhập mã OTP này để xác minh tài khoản của bạn.</p>
	            </div>
	            """.formatted(otp), true);
	    
	    javaMailSender.send(mimeMessage);
	}

	
	public void sendForgotMail(String email, String otp) throws MessagingException{
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,  MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("Xác nhận mã OTP");
		mimeMessageHelper.setText("""
				<div style="font-family: Arial, sans-serif; line-height: 1.5;">
					Chào bạn,
					Bạn đã vô tình quên mật khẩu. Đây là mã OTP của bạn:
					<h2 style="color: #2E86C1;">%s</h2>
					Vui lòng nhập mã OTP này để xác minh tài khoản của bạn
				</div>
				""".formatted(otp), true);
		javaMailSender.send(mimeMessage);
	}
}

