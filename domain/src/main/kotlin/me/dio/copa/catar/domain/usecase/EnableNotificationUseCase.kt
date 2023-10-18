package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class EnableNotificationUseCase @Inject constructor(
    private val matchesRepo: MatchesRepository
) {
    suspend operator fun invoke(id: String) = matchesRepo.enableNotificationFor(id)
}