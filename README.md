# Power Control

> [!CAUTION]
> **Power Control** is currently in development and may have bugs. Some of the features mentioned
> below, or the app for certain operating systems, may not be available in the current state.
>
>Please use the application with caution and report any issues via
> the [Issues](https://github.com/OffRange/PowerControl/issues) page.

Power Control is an Android application that allows you to remotely manage the power state of your
PCs. With this app, you can perform the following operations on your computer:

- **Shutdown**
- **Reboot**
- **Log-Out**
- **Boot** (using Wake-On LAN)

### Prerequisites

For operations like shutdown, reboot, and log-out, the target PC must have the **Power Control
Server** installed. The server application is available for the following operating systems:

- Windows
- Linux
- MacOS

You can find the Power Control Server on a separate GitHub
repository: [Power Control Server](https://github.com/OffRange/PowerControlServer)

To boot a PC using this app, ensure that your system is configured to support **Wake-On LAN**.

## Features

- **Remote Shutdown**: Turn off your PC remotely using your Android device.
- **Remote Reboot**: Restart your PC from anywhere.
- **Remote Log-Out**: Log out from the current session on your PC.
- **Wake-On LAN**: Boot your PC over the network.

## Installation

1. Download and install the Power Control app from the Google Play Store (link to be provided once
   available).
2. Install the Power Control Server on the target PC. Follow the instructions on
   the [Power Control Server GitHub page](https://github.com/OffRange/PowerControlServer).
4. Open the Power Control app and connect it to your PC by providing the necessary IP address.

## Configuration

- Ensure your PC is connected to the same network as your Android device.
- Configure your PC for Wake-On LAN if you intend to use the boot feature. Detailed instructions can
  be found in your PC's BIOS/UEFI settings and your operating system's network settings.

> [!NOTE]
> Power Control Server must be running on your target PC for shutdown, reboot, and log-out
> operations.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your
improvements.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
