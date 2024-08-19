package com.hungbang.email2018.data.entity;


/**
 * Created by Hungnd on 7/15/2017.
 */

public class SignInConfigs {
    public static final String FIELD_MAIL_DOMAIN = "mail_domain";

    public String mail_domain;
    public String imap_host;
    public String imap_port;
    public String imap_ssl;
    public String smtp_host;
    public String smtp_port;
    public String smtp_start_tls;

    public SignInConfigs() {
    }

    public SignInConfigs(String mail_domain, String imap_host, String imap_port, String imap_ssl, String smtp_host, String smtp_port, String smtp_start_tls) {
        this.mail_domain = mail_domain;
        this.imap_host = imap_host;
        this.imap_port = imap_port;
        this.imap_ssl = imap_ssl;
        this.smtp_host = smtp_host;
        this.smtp_port = smtp_port;
        this.smtp_start_tls = smtp_start_tls;
    }

    public String getMail_domain() {
        return mail_domain;
    }

    public void setMail_domain(String mail_domain) {
        this.mail_domain = mail_domain;
    }

    public String getImap_host() {
        return imap_host;
    }

    public void setImap_host(String imap_host) {
        this.imap_host = imap_host;
    }

    public String getImap_port() {
        return imap_port;
    }

    public void setImap_port(String imap_port) {
        this.imap_port = imap_port;
    }

    public String getImap_ssl() {
        return imap_ssl;
    }

    public void setImap_ssl(String imap_ssl) {
        this.imap_ssl = imap_ssl;
    }

    public String getSmtp_host() {
        return smtp_host;
    }

    public void setSmtp_host(String smtp_host) {
        this.smtp_host = smtp_host;
    }

    public String getSmtp_port() {
        return smtp_port;
    }

    public void setSmtp_port(String smtp_port) {
        this.smtp_port = smtp_port;
    }

    public String getSmtp_start_tls() {
        return smtp_start_tls;
    }

    public void setSmtp_start_tls(String smtp_start_tls) {
        this.smtp_start_tls = smtp_start_tls;
    }
}
