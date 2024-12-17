package org.example.eiscuno.controller.decorator;

import javafx.scene.control.Button;

/**
 * The ButtonDecorator interface defines a contract for classes that decorate a Button.
 * Any class that implements this interface must provide an implementation of the decorate method
 * to modify the appearance or behavior of a Button.
 */
public interface ButtonDecorator {

    /**
     * Decorates the given button by applying specific style or behavior.
     *
     * @param button The button to be decorated.
     */
    void decorate(Button button);
}
