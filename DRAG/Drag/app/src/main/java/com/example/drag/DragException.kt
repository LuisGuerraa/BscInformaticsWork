package com.example.drag

public class DragException (val type: DragInputValidation) : Exception() {
    //TODO use DragExceptions
    enum class DragInputValidation {
        INVALID_NUMBER_OF_PLAYERS, INVALID_NUMBER_OF_ROUNDS, INVALID_WORD
    }
}
