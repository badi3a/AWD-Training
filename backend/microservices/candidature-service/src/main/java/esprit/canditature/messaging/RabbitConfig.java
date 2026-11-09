package esprit.canditature.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topologie côté PRODUCTEUR.
 *
 * Règle : le producteur possède l'exchange, chaque consommateur possède SA queue.
 * candidature déclare donc l'exchange "jobboard.events" mais ne connaît aucune queue :
 * il ignore qui écoute ses événements (notification aujourd'hui, meeting demain...).
 */
@Configuration
public class RabbitConfig {

    /** Exchange de type topic qui reçoit tous les événements métier du JobBoard. */
    public static final String EXCHANGE = "jobboard.events";

    /** Clé de routage : <entité>.<fait au passé>. */
    public static final String APPLICATION_SUBMITTED_KEY = "application.submitted";

    /** Déclaré automatiquement sur RabbitMQ (par RabbitAdmin) à la première connexion. */
    @Bean
    public TopicExchange jobboardEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false); // durable, pas d'auto-suppression
    }

    /**
     * Messages en JSON (et non en sérialisation Java) : le consommateur est écrit en Python.
     * On réutilise l'ObjectMapper de Spring Boot : les dates partent au format ISO ("2026-10-18").
     * Spring Boot associe automatiquement ce bean au RabbitTemplate.
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
