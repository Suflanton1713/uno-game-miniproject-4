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
     * Extends {@code IndexOutOfBoundsException}.
     * @version 1.0
     */
    public static class OutOfCardsInDeck extends IllegalStateException{
        /**
         * Default constructor for the {@code OutOfBoardAction}.
         * @version 1.0
         */
        public OutOfCardsInDeck() {
            super("No hay más cartas en el mazo");
        }

        /**
         * Constructor with a custom error message for the {@code OutOfBoardAction}.
         * @param message the custom error message.
         * @version 1.0
         */
        public OutOfCardsInDeck(String message) {
            super("No hay más cartas en el mazo. " + message);
        }
    }

    /**
     * Exception for handling attempts to place a boat in an occupied space.
     * Extends {@code Exception}.
     * @version 1.0
     */
    public static class OccupiedBox extends Exception {
        /**
         * Default constructor for the {@code OccupiedBox}.
         * @version 1.0
         */
        public OccupiedBox() {
            super();
        }

        /**
         * Constructor with a custom error message for the {@code OccupiedBox}.
         * @param message the custom error message.
         * @version 1.0
         */
        public OccupiedBox(String message) {
            super(message);
        }

        /**
         * Constructor with a custom error message and cause for the {@code OccupiedBox}.
         * @param message the custom error message.
         * @param cause the cause of the exception.
         * @version 1.0
         */
        public OccupiedBox(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructor with a cause for the {@code OccupiedBox}.
         * @param cause the cause of the exception.
         * @version 1.0
         */
        public OccupiedBox(Throwable cause) {
            super(cause);
        }
    }

    /**
     * Exception for handling attempts to shoot a box that has already been shot.
     * Extends {@code IndexOutOfBoundsException}.
     * @version 1.0
     */
    public static class BoxAlreadyActivated extends IndexOutOfBoundsException {
        /**
         * Default constructor for the {@code BoxAlreadyActivated}.
         * @version 1.0
         */
        public BoxAlreadyActivated() {
            super();
        }

        /**
         * Constructor with a custom error message for the {@code BoxAlreadyActivated}.
         * @param message the custom error message.
         * @version 1.0
         */
        public BoxAlreadyActivated(String message) {
            super(message);
        }
    }

    /**
     * Exception for handling attempts to use a boat that has already been used.
     * Extends {@code NullPointerException}.
     * @version 1.0
     */
    public static class boatAlreadyUsed extends NullPointerException {
        /**
         * Default constructor for the {@code boatAlreadyUsed}.
         * @version 1.0
         */
        public boatAlreadyUsed() {
            super();
        }

        /**
         * Constructor with a custom error message for the {@code boatAlreadyUsed}.
         * @param message the custom error message.
         * @version 1.0
         */
        public boatAlreadyUsed(String message) {
            super(message);
        }
    }

    /**
     * Exception for handling attempts to access parts of a boat that are inaccessible.
     * Extends {@code IndexOutOfBoundsException}.
     * @version 1.0
     */
    public static class InaccessiblePartInBoat extends IndexOutOfBoundsException {
        /**
         * Default constructor for the {@code InaccessiblePartInBoat}.
         * @version 1.0
         */
        public InaccessiblePartInBoat() {
            super();
        }

        /**
         * Constructor with a custom error message for the {@code InaccessiblePartInBoat}.
         * @param message the custom error message.
         * @version 1.0
         */
        public InaccessiblePartInBoat(String message) {
            super(message);
        }
    }

    /**
     * Exception for handling attempts to access a position out of board bounds.
     * Extends {@code IndexOutOfBoundsException}.
     * @version 1.0
     */
    public static class OutOfBoardPosition extends IndexOutOfBoundsException {
        /**
         * Default constructor for the {@code OutOfBoardPosition}.
         * @version 1.0
         */
        public OutOfBoardPosition() {
            super("Fatal error occurred. You tried to access a box out of the board.");
        }

        /**
         * Constructor with a custom error message for the {@code OutOfBoardPosition}.
         * @param message the custom error message.
         * @version 1.0
         */
        public OutOfBoardPosition(String message) {
            super("Fatal error occurred. You tried to access a box out of the board. " + message + OutOfBoardPosition.class.getSimpleName());
        }
    }

    /**
     * Exception for handling situations where no board is found.
     * Extends {@code NullPointerException}.
     * @version 1.0
     */
    public static class NoBoardFound extends NullPointerException {
        /**
         * Default constructor for the {@code NoBoardFound}.
         * @version 1.0
         */
        public NoBoardFound() {
            super("Fatal error occurred about board accessing.");
        }

        /**
         * Constructor with a custom error message for the {@code NoBoardFound}.
         * @param message the custom error message.
         * @version 1.0
         */
        public NoBoardFound(String message) {
            super("Fatal error occurred about board accessing." + message + NoBoardFound.class.getSimpleName());
        }
    }

    /**
     * Exception for handling failures when saving a profile.
     * Extends {@code IOException}.
     * @version 1.0
     */
    public static class CantSaveProfile extends IOException {
        /**
         * Default constructor for the {@code CantSaveProfile}.
         * @version 1.0
         */
        public CantSaveProfile() {
            super("Fatal error occurred on saving profile.");
        }

        /**
         * Constructor with a custom error message for the {@code CantSaveProfile}.
         * @param message the custom error message.
         * @version 1.0
         */
        public CantSaveProfile(String message) {
            super("Fatal error occurred on saving profile. " + message);
        }
    }

    /**
     * Exception for handling situations where profiles do not exist.
     * Extends {@code NullPointerException}.
     * @version 1.0
     */
    public static class profilesDoesNotExist extends NullPointerException {
        /**
         * Default constructor for the {@code profilesDoesNotExist}.
         * @version 1.0
         */
        public profilesDoesNotExist() {
            super("Fatal error occurred on searching profile.");
        }

        /**
         * Constructor with a custom error message for the {@code profilesDoesNotExist}.
         * @param message the custom error message.
         * @version 1.0
         */
        public profilesDoesNotExist(String message) {
            super("Fatal error occurred on searching profile. " + message);
        }
    }

    /**
     * Exception for handling failures when loading a profile.
     * Extends {@code IOException}.
     * @version 1.0
     */
    public static class CantLoadProfile extends IOException {
        /**
         * Default constructor for the {@code CantLoadProfile}.
         * @version 1.0
         */
        public CantLoadProfile() {
            super("Fatal error occurred on loading profile.");
        }

        /**
         * Constructor with a custom error message for the {@code CantLoadProfile}.
         * @param message the custom error message.
         * @version 1.0
         */
        public CantLoadProfile(String message) {
            super("Fatal error occurred on loading profile. " + message);
        }
    }

    /**
     * Exception for handling failures when deleting a file.
     * Extends {@code IOException}.
     * @version 1.0
     */
    public static class CantDeleteFile extends IOException {
        /**
         * Default constructor for the {@code CantDeleteFile}.
         * @version 1.0
         */
        public CantDeleteFile() {
            super("Fatal error when deleting serializable file.");
        }

        /**
         * Constructor with a custom error message for the {@code CantDeleteFile}.
         * @param message the custom error message.
         * @version 1.0
         */
        public CantDeleteFile(String message) {
            super("Fatal error when deleting serializable file. " + message);
        }
    }

    /**
     * Exception for handling failures when loading a match.
     * Extends {@code NullPointerException}.
     * @version 1.0
     */
    public static class CantLoadMatch extends NullPointerException {
        /**
         * Default constructor for the {@code CantLoadMatch}.
         * @version 1.0
         */
        public CantLoadMatch() {
            super("Fatal error when loading match.");
        }

        /**
         * Constructor with a custom error message for the {@code CantLoadMatch}.
         * @param message the custom error message.
         * @version 1.0
         */
        public CantLoadMatch(String message) {
            super("Fatal error when loading match. " + message);
        }
    }
}

