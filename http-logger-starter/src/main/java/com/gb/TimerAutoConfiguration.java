package com.gb;


import com.gb.timer.TimerAspect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(LoggingProperties.class)


public class TimerAutoConfiguration {

    @Bean
    public TimerAspect timerAspect(LoggingProperties loggingProperties) {
        return new TimerAspect();
    }


}
