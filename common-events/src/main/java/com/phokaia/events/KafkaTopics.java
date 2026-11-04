package com.phokaia.events;

/**
 * Kafka topic names shared by all services.
 * Producers and consumers must use these constants, never hand-typed strings.
 */
public final class KafkaTopics {

    // Container only: never instantiated
    private KafkaTopics() { }

    /** transaction-service -> account-service: a client asked for a transfer */
    public static final String TRANSACTION_REQUESTED = "transaction.requested";

    /** account-service -> transaction-service, notification-service: money was moved */
    public static final String TRANSACTION_COMPLETED = "transaction.completed";

    /** account-service -> transaction-service, notification-service: the transfer was rejected */
    public static final String TRANSACTION_FAILED = "transaction.failed";
}