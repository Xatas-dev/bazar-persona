package org.bazar.persona.utils.extensions

import java.net.URI
import java.util.UUID

fun String.toUuid() : UUID = UUID.fromString(this)

fun String.toUri() : URI = URI.create(this)