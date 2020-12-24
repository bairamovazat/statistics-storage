package ru.ivmiit.web.service;

public interface EmailService {
    void sendMail(String text, String subject, String email);
}
