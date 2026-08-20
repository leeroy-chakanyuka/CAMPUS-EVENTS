package za.ac.cput.campus_events.DTO;

public class SendNotificationRequestDTO {
    private String message;
    private Long recipientId;
    private String recipientType;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }
}
