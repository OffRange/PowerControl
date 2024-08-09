package de.davis.powercontrol.core.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.usecases.GetDeviceStatusUseCase
import de.davis.powercontrol.core.domain.usecases.LogoutUseCase
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase
import de.davis.powercontrol.core.domain.usecases.WakeOnLanUseCase
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class PowerControlWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {

    private val shutdown: ShutdownUseCase by inject()
    private val boot: WakeOnLanUseCase by inject()
    private val logout: LogoutUseCase by inject()

    private val getDeviceStatus: GetDeviceStatusUseCase by inject()
    private val deviceRepository: DeviceRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val ip = inputData.getString(IP_KEY) ?: return@withContext Result.failure()
        val operationType = inputData.getString(OPERATION_KEY)?.let { PowerOperation.valueOf(it) }
            ?: return@withContext Result.failure()

        val device = deviceRepository.getDeviceByIp(ip) ?: return@withContext Result.failure()

        when (operationType) {
            PowerOperation.Shutdown -> {
                if (getDeviceStatus(device) == DeviceStatus.Online)
                    shutdown(device)
            }

            PowerOperation.Boot -> {
                if (getDeviceStatus(device) == DeviceStatus.Offline)
                    boot(device)
            }

            PowerOperation.Restart -> {}
            PowerOperation.Logout -> {
                logout(device)
            }
        }

        Result.success()
    }


    companion object {
        private const val BASE_WORK_NAME = "PowerControlWorker"
        const val IP_KEY = "ip"
        const val OPERATION_KEY = "operation"

        fun IpAddress.asWorkerName() = "$BASE_WORK_NAME-$this"

        fun schedule(
            operation: PowerOperation,
            ip: IpAddress,
            initialDelay: Long,
            workManager: WorkManager
        ) {
            val workRequest = OneTimeWorkRequestBuilder<PowerControlWorker>().apply {
                setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                setInputData(
                    workDataOf(
                        IP_KEY to ip,
                        OPERATION_KEY to operation.name
                    )
                )
                setConstraints(
                    Constraints.Builder().apply {
                        setRequiredNetworkType(NetworkType.CONNECTED)
                    }.build()
                )
                addTag(operation.name)
            }.build()

            workManager.enqueueUniqueWork(
                ip.asWorkerName(),
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}