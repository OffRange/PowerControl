package de.davis.powercontrol.core.domain.network.mapper

import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import java.net.InetAddress

fun IpAddress.toInetAddress(): InetAddress = InetAddress.getByName(this)