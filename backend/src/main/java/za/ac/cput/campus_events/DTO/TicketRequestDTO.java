package za.ac.cput.campus_events.DTO;

public class TicketRequestDTO {
    private Long eventId;
    private String promoCode;
    private double price;

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
