package org.example.eiscuno.model.exception;


import java.io.IOException;

/**
 * Custom exception class for handling various game-related exceptions.
 * Includes nested static classes to handle specific error cases during gameplay.
 * @author  Libardo Alejandro Quintero, Juan David Rincon Lopez, Daniel Andrade.
 * @version 1.0
 */
public class GameException extends Exception {

    /**
     * Default constructor for the {@code GameException}.
     * @version 1.0
     */
    public GameException() {
        super();
    }

    /**
     * Constructor with a custom error message for the {@code GameException}.
     * @param message the custom error message.
     * @version 1.0
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause for the {@code GameException}.
     * @param message the custom error message.
     * @param cause the cause of the exception.
     * @version 1.0
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause for the {@code GameException}.
     * @param cause the cause of the exception.
     * @version 1.0
     */
    public GameException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception when the deck runs out of cards.
     * Extends {@code IllegalStateException}.
     * @version 1.0
     */
    public static class OutOfCardsInDeck extends IllegalStateException{
        /**
         * Default constructor for the {@code OutOfCardsInDeck}.
         * @version 1.0
         */
        public OutOfCardsInDeck() {
            super("Deck has run out of cards");
        }

        /**
         * Constructor with a custom error message for the {@code OutOfCardsInDeck}.
         * @param message the custom error message.
         * @version 1.0
         */
        public OutOfCardsInDeck(String message) {
            super("Deck has run out of cards. " + message);
        }
    }

    /**
     * Exception for handling illegal colors in cards.
     * Extends {@code IllegalStateException}.
     * @version 1.0
     */
    public static class IllegalCardColor extends IllegalArgumentException {
        /**
         * Default constructor for the {@code IllegalCardColor}.
         * @version 1.0
         */
        public IllegalCardColor() {
            super("The previous card has an illegal color. ");
        }

        /**
         * Constructor with a custom error message for the {@code IllegalCardColor}.
         * @param message the custom error message.
         * @version 1.0
         */
        public IllegalCardColor(String message) {
            super("The previous card has an illegal color. " +message);
        }
    }

    /**
     * Exception for handling illegal values in cards.
     * Extends {@code IllegalStateException}.
     * @version 1.0
     */
    public static class IllegalCardValue extends IllegalArgumentException {
        /**
         * Default constructor for the {@code IllegalCardValue}.
         * @version 1.0
         */
        public IllegalCardValue() {
            super("The previous card has an illegal value. ");
        }

        /**
         * Constructor with a custom error message for the {@code IllegalCardValue}.
         * @param message the custom error message.
         * @version 1.0
         */
        public IllegalCardValue(String message) {
            super("The previous card has an illegal value. " +message);
        }
    }

    /**
     * Exception for handling actions when game have ended.
     * Extends {@code IllegalStateException}.
     * @version 1.0
     */
    public static class gameEndedException extends IllegalStateException {
        /**
         * Default constructor for the {@code gameEndedException}.
         * @version 1.0
         */
        public gameEndedException() {
            super("The game has already ended. Couldn't perform the action. ");
        }

        /**
         * Constructor with a custom error message for the {@code gameEndedException}.
         * @param message the custom error message.
         * @version 1.0
         */
        public gameEndedException(String message) {
            super("The game has already ended. Couldn't perform the action. " +message);
        }
    }

}

