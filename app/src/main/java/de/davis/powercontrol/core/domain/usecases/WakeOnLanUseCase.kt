package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.network.datagramSocket
import de.davis.powercontrol.core.domain.network.mapper.toInetAddress
import de.davis.powercontrol.core.domain.network.sendPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WakeOnLanUseCase {

    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(device: Device) = withContext(Dispatchers.IO) {
        device.mac ?: return@withContext false

        val inetAddress = device.ip.toInetAddress()

        val magicPacket = ByteArray(102) {
            if (it < 6) 0xff.toByte()
            else device.mac.split(":")[(it - 6) % 6].hexToByte()
        }

        datagramSocket {
            sendPacket(
                data = magicPacket,
                serverAddress = inetAddress,
                port = PORT
            )
        }
        return@withContext true // TODO
    }

    companion object {
        const val PORT = 9
    }
}