package de.davis.powercontrol.core.domain.network

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun <R> datagramSocket(block: DatagramSocket.() -> R): R = DatagramSocket().use(block)

fun DatagramSocket.sendPacket(
    data: ByteArray,
    length: Int = data.size,
    serverAddress: InetAddress,
    port: Int
) {
    runCatching {
        send(DatagramPacket(data, length, serverAddress, port))
    }
}

fun DatagramSocket.receivePacket(
    buf: ByteArray,
    timeout: Int
): Result<DatagramPacket> {
    soTimeout = timeout
    return runCatching {
        DatagramPacket(buf, buf.size).also {
            receive(it)
        }
    }
}