package dev.jordond.kmpalette

public class Greeting {

    private val platform: Platform = object : Platform {
        override val name: String = "TODO"
    }

    public fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}