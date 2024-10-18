package com.sgalera.gaztelubira.domain.usecases

import com.sgalera.gaztelubira.domain.repository.AppRepository
import javax.inject.Inject

class CanAccessAppUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(): Boolean {
        val minVersionAllowed = appRepository.getMinAllowedVersion()
        val appVersion = appRepository.getAppVersion()
        return appVersion.zip(minVersionAllowed).all { (appVersion, minVersion) -> appVersion >= minVersion }
    }
}