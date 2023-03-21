package com.startup.comexcase_api.api.events;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter @Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private DealerEntity dealer;

    public OnRegistrationCompleteEvent(DealerEntity dealer, Locale locale, String appUrl) {
        super(dealer);

        this.appUrl = appUrl;
        this.dealer = dealer;
        this.locale = locale;
    }
}
