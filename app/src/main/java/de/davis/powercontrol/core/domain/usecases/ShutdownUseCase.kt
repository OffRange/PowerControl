package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.models.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class ShutdownUseCase {

    suspend operator fun invoke(device: Device): Boolean = withContext(Dispatchers.IO) {
        val inetAddress = InetAddress.getByName(device.ip)

        val buffer = ByteArray(1)
        datagramSocket {
            sendPacket(
                data = byteArrayOf(device.shutdownSequence),
                serverAddress = inetAddress,
                serverPort = device.port.toUShort()
            )

            val pack = receivePacket(buffer, TIMEOUT.toInt(DurationUnit.MILLISECONDS))
            //TODO receive and send feedback to user
        }
        return@withContext true // TODO
    }

    companion object {
        val TIMEOUT: Duration = 5.seconds
    }
}

private fun datagramSocket(block: DatagramSocket.() -> Unit) {
    DatagramSocket().use(block)
}

private fun DatagramSocket.sendPacket(
    data: ByteArray,
    length: Int = data.size,
    serverAddress: InetAddress,
    serverPort: UShort
) {
    send(DatagramPacket(data, length, serverAddress, serverPort.toInt()))
}

private fun DatagramSocket.receivePacket(
    buf: ByteArray,
    timeout: Int
): DatagramPacket {
    soTimeout = timeout
    return DatagramPacket(buf, buf.size).also {
        runCatching {
            receive(it)
        }
    }
}