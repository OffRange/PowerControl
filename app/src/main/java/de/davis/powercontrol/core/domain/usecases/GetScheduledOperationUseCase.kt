package de.davis.powercontrol.core.domain.usecases

import androidx.work.WorkInfo
import androidx.work.WorkManager
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.domain.models.ScheduledOperation
import de.davis.powercontrol.core.worker.PowerControlWorker.Companion.asWorkerName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class GetScheduledOperationUseCase(private val workManager: WorkManager) {

    suspend operator fun invoke(device: Device): ScheduledOperation = withContext(Dispatchers.IO) {
        workManager.getWorkInfosForUniqueWork(device.ip.asWorkerName()).get()
            .firstOrNull { it.state == WorkInfo.State.ENQUEUED }
            ?.let { workInfo ->
                workInfo.tags.firstNotNullOfOrNull { tag ->
                    PowerOperation.entries.find { it.name == tag }
                }?.let { operation ->
                    ScheduledOperation.Scheduled(
                        operation = operation,
                        time = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(workInfo.nextScheduleTimeMillis),
                            ZoneId.systemDefault()
                        )
                    )
                }
            } ?: ScheduledOperation.None
    }
}