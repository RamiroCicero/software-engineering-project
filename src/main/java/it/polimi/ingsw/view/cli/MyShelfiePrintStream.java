package it.polimi.ingsw.view.cli;
import java.io.PrintStream;

/**
 * The MyShelfiePrintStream class extends the PrintStream class and provides custom functionality for printing.
 * It inherits from the standard output stream (System.out).
 */
public class MyShelfiePrintStream extends PrintStream {

    /**
     * Constructs a new MyShelfiePrintStream object.
     * The new print stream will be initialized to write to the standard output stream (System.out) with auto-flush enabled.
     */
    MyShelfiePrintStream() {
        super(System.out, true);

}}