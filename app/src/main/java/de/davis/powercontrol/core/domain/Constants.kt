package de.davis.powercontrol.core.domain

const val DEFAULT_SHUTDOWN_SEQUENCE = 0xa5.toByte()
const val DEFAULT_PORT = 7848

const val HEARTBEAT_BYTE = 0x00.toByte()
const val SHUTDOWN_BYTE = 0x01.toByte()
const val RESTART_BYTE = 0x02.toByte()
const val LOGOUT_BYTE = 0x03.toByte()