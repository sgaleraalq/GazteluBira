import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

object PlayerMapper {
    suspend fun mapPlayerInformation(player: String): PlayerInformation? {
        return try {
            val apiService = PlayersApiService(firebase = FirebaseClient())
            apiService.playerInformation(player)
        } catch (e: Exception) {
            PlayerInformation(
                name = "Error",
                completeName = "Error",
                img = "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/img_no_profile_picture.webp?alt=media&token=3704b325-2ffc-42eb-892d-147c3860d8b1",
                dorsal = 0,
                stats = null,
                selected = false
            )
        }
    }
}
