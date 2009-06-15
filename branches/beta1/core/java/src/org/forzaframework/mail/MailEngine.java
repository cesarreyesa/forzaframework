/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Map;
import java.util.Properties;
import java.security.Security;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:03:01
 */
public class MailEngine {
    
    protected static final Log log = LogFactory.getLog(MailEngine.class);
    private MailSender mailSender;
    private VelocityEngine velocityEngine;
    private Boolean debug = false;

    public MailEngine() {
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * Send a simple message based on a Velocity template.
     * @param msg
     * @param templateName
     * @param model
     */
    public void sendMessage(SimpleMailMessage msg, String templateName, Map model) {
        String result = null;

        try {
            result =
                VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                            templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
        }

        msg.setText(result);
        send(msg);
    }

    /**
     * Send a simple message with pre-populated values.
     * @param msg
     */
    public void send(SimpleMailMessage msg) {
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            //log it and go on
            log.error(ex.getMessage());
        }
    }

    /**
     * Convenience method for sending messages with attachments.
     *
     * @param emailAddresses
     * @param resource
     * @param bodyText
     * @param subject
     * @param attachmentName
     * @throws MessagingException
     * @author Ben Gill
     */
    public void sendMessage(String[] emailAddresses,
                            ClassPathResource resource, String bodyText,
                            String subject, String attachmentName)
    throws MessagingException {
        MimeMessage message =
            ((JavaMailSenderImpl) mailSender).createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailAddresses);
        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, resource);

        ((JavaMailSenderImpl) mailSender).send(message);
    }

    public void sendMessage(String[] emailAddresses, InputStreamSource inputStreamSource, String bodyText, String subject, String attachmentName) throws MessagingException {
        MimeMessage message =
            ((JavaMailSenderImpl) mailSender).createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(emailAddresses);
        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, inputStreamSource);

        ((JavaMailSenderImpl) mailSender).send(message);
    }

    public void sendMessage(final SimpleMailMessage msg, final String templateName, final Map model, final boolean sendAsHTML) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom(msg.getFrom());
                message.setTo(msg.getTo());
                message.setSubject(msg.getSubject());
                String result = null;

                try {
                    result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
                } catch (VelocityException e) {
                    e.printStackTrace();
                }
                if(sendAsHTML){
                    message.setText("<html><body>" + result + "</body></html>", true);
                }else{
                    message.setText(result, false);                    
                }
            }
        };
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            if(((JavaMailSenderImpl) mailSender).getHost().contains("gmail")){
                Properties props = new Properties();
                props.put("mail.smtp.host", ((JavaMailSenderImpl) mailSender).getHost());
                props.put("mail.smtp.auth", "true");
                props.put("mail.debug", getDebug().toString());
                props.put("mail.smtp.port", ((JavaMailSenderImpl) mailSender).getPort());
                props.put("mail.smtp.socketFactory.port", ((JavaMailSenderImpl) mailSender).getPort());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");

                Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(((JavaMailSenderImpl) mailSender).getUsername(), ((JavaMailSenderImpl) mailSender).getPassword());
                            }
                        });

                session.setDebug(getDebug());

                ((JavaMailSenderImpl) mailSender).setSession(session);
            }
            ((JavaMailSenderImpl) mailSender).send(preparator);
        }
        catch (MailException ex) {
            log.info(ex.getMessage());
        }
    }
}
