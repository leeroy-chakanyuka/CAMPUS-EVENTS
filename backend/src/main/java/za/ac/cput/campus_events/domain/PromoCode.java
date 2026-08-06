package za.ac.cput.campus_events.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
public class PromoCode {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String discountType;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private String scopeType;

    @Column(nullable = false)
    private int maxRedemptions;

    @Column(nullable = false)
    private int timesUsed;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public PromoCode() {
    }

    private PromoCode(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.discountType = builder.discountType;
        this.value = builder.value;
        this.scopeType = builder.scopeType;
        this.maxRedemptions = builder.maxRedemptions;
        this.timesUsed = builder.timesUsed;
        this.startDate = builder.startDate;
        this.expiryDate = builder.expiryDate;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public double getValue() {
        return value;
    }

    public String getScopeType() {
        return scopeType;
    }

    public int getMaxRedemptions() {
        return maxRedemptions;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isValidNow() {
        LocalDate today = LocalDate.now();

        return active
                && !today.isBefore(startDate)
                && !today.isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", discountType='" + discountType + '\'' +
                ", value=" + value +
                ", scopeType='" + scopeType + '\'' +
                ", maxRedemptions=" + maxRedemptions +
                ", timesUsed=" + timesUsed +
                ", startDate=" + startDate +
                ", expiryDate=" + expiryDate +
                ", active=" + active +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class Builder {

        private String id;
        private String code;
        private String discountType;
        private double value;
        private String scopeType;
        private int maxRedemptions;
        private int timesUsed;
        private LocalDate startDate;
        private LocalDate expiryDate;
        private boolean active;
        private LocalDateTime createdAt;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setDiscountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public Builder setValue(double value) {
            this.value = value;
            return this;
        }

        public Builder setScopeType(String scopeType) {
            this.scopeType = scopeType;
            return this;
        }

        public Builder setMaxRedemptions(int maxRedemptions) {
            this.maxRedemptions = maxRedemptions;
            return this;
        }

        public Builder setTimesUsed(int timesUsed) {
            this.timesUsed = timesUsed;
            return this;
        }

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setExpiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder copy(PromoCode promoCode) {
            this.id = promoCode.id;
            this.code = promoCode.code;
            this.discountType = promoCode.discountType;
            this.value = promoCode.value;
            this.scopeType = promoCode.scopeType;
            this.maxRedemptions = promoCode.maxRedemptions;
            this.timesUsed = promoCode.timesUsed;
            this.startDate = promoCode.startDate;
            this.expiryDate = promoCode.expiryDate;
            this.active = promoCode.active;
            this.createdAt = promoCode.createdAt;
            return this;
        }

        public PromoCode build() {
            return new PromoCode(this);
        }
    }
}