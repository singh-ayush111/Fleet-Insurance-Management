package com.htc.fleetmanagement.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Utility class to load email templates from the classpath
 * Templates are stored in src/main/resources/templates/ directory
 */
@Component
public class EmailTemplateLoader {

	private static final Logger LOGGER = Logger.getLogger(EmailTemplateLoader.class.getName());
	
	private static final String TEMPLATE_DIR = "templates/";
	private static final String CLAIM_NOTIFICATION_TEMPLATE = "claim-notification-email.html";
	private static final String CLAIM_STATUS_UPDATE_TEMPLATE = "claim-status-update-email.html";


	public String loadClaimNotificationTemplate() throws IOException {
		try {
			return loadTemplate(CLAIM_NOTIFICATION_TEMPLATE);
		} catch (IOException e) {
			LOGGER.severe("Failed to load claim notification template: " + e.getMessage());
			throw new IOException("Failed to load claim notification email template", e);
		}
	}


	public String loadClaimStatusUpdateTemplate() throws IOException {
		try {
			return loadTemplate(CLAIM_STATUS_UPDATE_TEMPLATE);
		} catch (IOException e) {
			LOGGER.severe("Failed to load claim status update template: " + e.getMessage());
			throw new IOException("Failed to load claim status update email template", e);
		}
	}


	private String loadTemplate(String templateName) throws IOException {
		try {
			ClassPathResource resource = new ClassPathResource(TEMPLATE_DIR + templateName);
			byte[] bytes = Files.readAllBytes(Paths.get(resource.getFile().getAbsolutePath()));
			return new String(bytes, "UTF-8");
		} catch (IOException e) {
			LOGGER.severe("Error loading template " + templateName + ": " + e.getMessage());
			throw new IOException("Cannot load template: " + templateName, e);
		}
	}
}
