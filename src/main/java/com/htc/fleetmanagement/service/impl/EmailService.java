package com.htc.fleetmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.FleetClaim;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.util.ClaimStatus;
import com.htc.fleetmanagement.util.EmailTemplateLoader;
import com.htc.fleetmanagement.repository.FleetManagerRepository;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FleetManagerRepository fleetManagerRepository;
    
    @Autowired
	private PDFGeneratorService pdfGeneratorService;
    
    @Autowired
    private EmailTemplateLoader emailTemplateLoader;

    @Async
    public void sendClaimNotificationToManager(FleetClaim claim, Driver driver) throws DocumentException {
    	
        Integer clientId = driver.getClient().getUserId();

        // Fetch the fleet manager associated with the client's user ID
        FleetManager fleetManager = fleetManagerRepository
                .findByClient_UserId(clientId)
                .orElse(null);

        if (fleetManager == null) {
            System.out.println("No fleet manager found for client ID: "
                    + clientId + ". Skipping email.");
            return;
        }

        // Generate PDF and send email
        try {
        	
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            
            byte[] pdfBytes = pdfGeneratorService.generateClaimPdf(claim, driver);
            
            
            helper.setFrom(driver.getEmail());        
            helper.setTo(fleetManager.getEmail());    
            helper.setSubject("New Fleet Claim Filed - Claim #" + claim.getClaimId());

            // Load HTML template from file
            String htmlTemplate = emailTemplateLoader.loadClaimNotificationTemplate();
            
            String htmlContent = htmlTemplate.formatted(
                        fleetManager.getName(),                  
                        claim.getClaimId(),                      
                        driver.getName(),                        
                        driver.getEmail(),                       
                        claim.getVehicle().getVin(),             
                        claim.getIncidentDate().toString(),      
                        claim.getRepairCost().toString(),        
                        claim.getStatus().toString(),            
                        java.time.LocalDateTime.now().toString() 
                    );

            helper.setText(htmlContent, true);
            
            // Attach the PDF to the email
            helper.addAttachment(
                    "Claim-" + claim.getClaimId() + "-" + driver.getName().replace(" ", "_") + ".pdf",
                    new org.springframework.core.io.ByteArrayResource(pdfBytes),
                    "application/pdf"
            );
            mailSender.send(message);

            System.out.println("Claim notification sent from "
                    + driver.getEmail() + " to " + fleetManager.getEmail());

        } catch (MessagingException e) {
            System.out.println("Mail Sending Failed for Claim #"
                    + claim.getClaimId() + " : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error sending claim notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Async
    public void sendClaimStatusUpdateToManager(FleetClaim claim, ClaimStatus newStatus) {
        Integer clientId = claim.getVehicle().getClient().getUserId();

        // Fetch the fleet manager associated with the client's user ID
        FleetManager fleetManager = fleetManagerRepository
                .findByClient_UserId(clientId)
                .orElse(null);

        if (fleetManager == null) {
            System.out.println("No fleet manager found for client ID: "
                    + clientId + ". Skipping email.");
            return;
        }

        // Generate and send email about claim status update
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@fleetmanagement.com");
            helper.setTo(fleetManager.getEmail());
            
            // Set subject based on status
            String subject = newStatus == ClaimStatus.APPROVED 
                ? "Claim Approved - Claim #" + claim.getClaimId()
                : "Claim Rejected - Claim #" + claim.getClaimId();
            helper.setSubject(subject);

            // Determine color and icon based on status
            String statusColor = newStatus == ClaimStatus.APPROVED ? "#27ae60" : "#e74c3c";
            String statusIcon = newStatus == ClaimStatus.APPROVED ? "✓" : "✗";

            // Load HTML template from file
            String htmlTemplate = emailTemplateLoader.loadClaimStatusUpdateTemplate();
            
            String htmlContent = htmlTemplate.formatted(
                        statusColor,                                    
                        newStatus.toString(),                           
                        fleetManager.getName(),                         
                        claim.getClaimId(),                            
                        claim.getDriver().getName(),                    
                        claim.getVehicle().getVin(),                   
                        claim.getVehicle().getMakeModel(),              
                        claim.getIncidentDate().toString(),             
                        claim.getRepairCost().toString(),               
                        statusColor,                                    
                        statusIcon,                                     
                        newStatus.toString(),                           
                        statusColor,                                   
                        statusColor,                                    
                        getSummaryMessage(newStatus),                   
                        java.time.LocalDateTime.now().toString()        
                    );

            helper.setText(htmlContent, true);
            mailSender.send(message);

            System.out.println("Claim status update sent to " + fleetManager.getEmail() 
                    + " for Claim #" + claim.getClaimId() + " - Status: " + newStatus);

        } catch (MessagingException e) {
            System.out.println("Mail Sending Failed for Claim Status Update #"
                    + claim.getClaimId() + " : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error sending claim status update email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private String getSummaryMessage(ClaimStatus status) {
        switch (status) {
            case APPROVED:
                return "The claim has been reviewed and APPROVED by the insurance agent. " +
                       "The associated vehicle is now available for use again.";
            case REJECTED:
                return "The claim has been reviewed and REJECTED by the insurance agent. " +
                       "Please review the claim details and contact the insurance agent for more information.";
            case PENDING:
                return "The claim is currently pending review by the insurance agent. " +
                       "You will receive an update once a decision has been made.";
            default:
                return "The claim status has been updated. Please check the claim details for more information.";
        }
    }
}
