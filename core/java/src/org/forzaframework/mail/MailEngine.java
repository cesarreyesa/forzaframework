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

import freemarker.template.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.forzaframework.orm.hibernate3.support.OpenSessionInThreadTask;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:03:01
 */
public class MailEngine implements ApplicationContextAware {
    
    protected static final Logger log = LogManager.getLogger(MailEngine.class);
    private MailSender mailSender;
    private Configuration freeMarkerConfiguration;
    private Boolean debug = false;
    private Boolean asynchronous = false;
    private TaskExecutor taskExecutor;
    private ApplicationContext ctx;

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Configuration getFreeMarkerConfiguration() {
        return freeMarkerConfiguration;
    }

    public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    public Boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(Boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
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
            StringBuffer content = new StringBuffer();
            try {
                content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate(templateName), model));
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = content.toString();

        } catch (Exception e) {
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
        sendMessage(msg, templateName, model, sendAsHTML, null);
    }

    public void sendMessage(final SimpleMailMessage msg, final String templateName, final Map model, final boolean sendAsHTML, final List<FileSystemResource> resources) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
                message.setFrom(msg.getFrom());
                message.setTo(msg.getTo());
                message.setSubject(msg.getSubject());

                if (resources != null) {
                    for (FileSystemResource resource : resources) {
                        message.addAttachment(resource.getFilename(), resource);
                    }
                }

                String result = null;

                try {
                    StringBuffer content = new StringBuffer();
                    try {
                        content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate(templateName, "UTF-8"), model));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = content.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(sendAsHTML){
                    message.setText("<html><body>" + result + "</body></html>", true);
                }else{
                    message.setText(result, false);
                }
            }
        };
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
        if(asynchronous){
            taskExecutor.execute(new MailSenderTask(mailSender, preparator, ctx));
        }else{
            ((JavaMailSenderImpl) mailSender).send(preparator);
        }
    }

    public class MailSenderTask extends OpenSessionInThreadTask {

        private MimeMessagePreparator preparator;
        private MailSender mailSender;

        public MailSenderTask(MailSender mailSender, MimeMessagePreparator preparator, ApplicationContext ctx) {
            super(ctx);
            this.mailSender = mailSender;
            this.preparator = preparator;
        }

        public void runInternal(){
            ((JavaMailSenderImpl) mailSender).send(preparator);
        }
    }
}
