class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello from KMP, ${platform.name}!"
    }
}
