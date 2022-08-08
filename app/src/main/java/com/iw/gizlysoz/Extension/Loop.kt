package com.iw.gizlysoz.Extension

class Loop {
    private var action: (Loop) -> Unit

    constructor(action: (Loop) -> Unit) {
        this.action = action
        this.action(this)
    }

    fun next() {
        this.action(this)
    }
}