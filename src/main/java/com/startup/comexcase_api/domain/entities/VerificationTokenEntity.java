package com.startup.comexcase_api.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "verification_token")
@Getter @Setter @NoArgsConstructor
public class VerificationTokenEntity {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = DealerEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "dealer_id")
    private DealerEntity dealer;

    @Column(name = "expiry_date")
    private Date expiryDate = calculateExpiryDate(60);

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}
