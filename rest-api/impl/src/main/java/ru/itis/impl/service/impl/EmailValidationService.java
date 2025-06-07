package ru.itis.impl.service.impl;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;

@Service
public class EmailValidationService {

    public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    public boolean isMailRuDomain(String email) {
        return email.toLowerCase().endsWith("@mail.ru");
    }

    public boolean isMailRuServerReachable() {
        try {
            Socket socket = new Socket("smtp.mail.ru", 465);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
} 