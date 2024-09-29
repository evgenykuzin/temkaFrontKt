infix fun <RS> (()->RS).tryOrElse(defaultValue: RS) = try {
    this()
} catch (t: Throwable) {
    t.printStackTrace()
    defaultValue
}