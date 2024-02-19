package com.gb;

import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data // getter + setter
@ConfigurationProperties("gb")
public class LoggingProperties {

  /**
   * Включить\выключить логиирование тела запроса
   */
  private boolean logBody = false;

  /**
   * Уровень логирования
   */
  private Level logLevel = Level.WARN;

}
