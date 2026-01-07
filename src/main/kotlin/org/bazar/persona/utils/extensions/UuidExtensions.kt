package org.bazar.persona.utils.extensions

import java.util.UUID

fun String.toUuid() : UUID = UUID.fromString(this)