package virtus.synergy.core

/**
 * Mind Tempus
 * Created on 29/05/2023.
 * Author: johan
 */
fun String.capitalizeFirstChar() = replaceFirstChar(Char::titlecase)

fun String.capitalize() = this.split(" ").joinToString(" ") { it.capitalizeFirstChar() }