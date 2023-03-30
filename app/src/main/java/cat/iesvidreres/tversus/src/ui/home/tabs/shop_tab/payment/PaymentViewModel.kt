package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.src.core.Event
import cat.iesvidreres.tversus.src.data.interfaces.tournamentAPI
import cat.iesvidreres.tversus.src.data.interfaces.userAPI
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.data.models.Tournament
import cat.iesvidreres.tversus.src.data.models.User
import cat.iesvidreres.tversus.src.data.providers.firebase.AuthenticationRepository
import cat.iesvidreres.tversus.src.ui.home.tabs.profile_tab.ProfileViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment.model.Payment
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewModel
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.CreateTournamentViewState
import cat.iesvidreres.tversus.src.ui.home.tabs.tournament_tab.create_tournament_tab.model.NewTournament
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PaymentViewModel @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    ): ViewModel() {

    private companion object{
        const val CARD_NUMBER_LENGTH = 16
        const val PIN_LENGTH = 4
        const val CADUCITY_LENGTH = 5
    }
    private var _shopCard: ShopCard? = null
    val shopCard get() = _shopCard

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome

    private val _viewState = MutableStateFlow(PaymentViewState())
    val viewState: StateFlow<PaymentViewState>
        get() = _viewState

    fun onFieldsChanged(payment: Payment) {
        _viewState.value = payment.toPaymentState()
    }

    fun setItemShop(shopCard: ShopCard) {
        _shopCard = shopCard
    }

    fun isValidCardNumber(number: String) = number.length >= CARD_NUMBER_LENGTH

    fun isValidPin(pin: String) = pin.length >= PIN_LENGTH

    fun isValidCaducity(caducity: String) = caducity.length >= CADUCITY_LENGTH

    fun Payment.toPaymentState(): PaymentViewState {
        return PaymentViewState(
            isValidCardNumber = isValidCardNumber(cardNumber),
            isValidCardCvv = isValidPin(cvv),
            isValidCardCaducity = isValidCaducity(caducity)
        )
    }

    fun onFinishSelected(context: Context, payment: Payment, shopCard: ShopCard){
        val viewState = payment.toPaymentState()
        if (viewState.paymentValidated() && payment.isNotEmpty()) {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(userAPI::class.java);
            var user: User
            api.getUserByEmail(authenticationRepository.getCurrentUserEmail().email.toString())
                .enqueue(object : Callback<User> {
                    @SuppressLint("SetTextI18n")
                    override fun onResponse(
                        call: Call<User>, response: Response<User>
                    ) {
                        user = response.body()!!
                        val oldTokens = user.tokens
                        val newTokens = shopCard.valor
                        val finalTokens = oldTokens + newTokens
                        user.tokens = finalTokens
                        api.buyTokens(authenticationRepository.getCurrentUserEmail().email.toString(), user).enqueue(object : Callback<User> {
                            @SuppressLint("SetTextI18n")
                            override fun onResponse(
                                call: Call<User>, response: Response<User>
                            ) {
                                user = response.body()!!
                            }
                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Log.i("Error", "$t")
                            }
                        })
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.i("Error", "$t")
                    }

                })
            _navigateToHome.value = Event(true)

        }else{
            onFieldsChanged(payment)
            Toast.makeText(context, "¡Completa todos los campos!", Toast.LENGTH_SHORT).show()
        }
    }


}