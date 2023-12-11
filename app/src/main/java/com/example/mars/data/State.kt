package com.example.mars.data

sealed class State {
    object Success : State()
    object Error : State()
}
