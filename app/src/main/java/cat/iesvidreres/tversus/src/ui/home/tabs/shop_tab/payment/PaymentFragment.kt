package cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.iesvidreres.tversus.R
import cat.iesvidreres.tversus.databinding.FragmentPaymentBinding
import cat.iesvidreres.tversus.src.core.ex.loseFocusAfterAction
import cat.iesvidreres.tversus.src.core.ex.onTextChanged
import cat.iesvidreres.tversus.src.core.ex.toast
import cat.iesvidreres.tversus.src.data.models.ShopCard
import cat.iesvidreres.tversus.src.ui.home.tabs.shop_tab.payment.model.Payment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private lateinit var shopCard: ShopCard

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        shopCard = paymentViewModel.shopCard!!

        binding.tvArticleValue.text = "Tokens: " + shopCard.valor.toString()
        binding.tvPrice.text = shopCard.price.toString() + " €"

        initUI()
        return binding.root
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {

        with(binding) {

            inputCardNumberText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputCardNumberText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputCardNumberText.onTextChanged { onFieldChanged() }

            inputCardCvvText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputCardCvvText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputCardCvvText.onTextChanged { onFieldChanged() }

            inputCardCaducityText.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            inputCardCaducityText.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            inputCardCaducityText.onTextChanged { onFieldChanged() }

            paymentViewModel.onFieldsChanged(
                Payment(
                    cardNumber = inputCardNumberText.text.toString(),
                    cvv = inputCardCvvText.text.toString(),
                    caducity = inputCardCaducityText.text.toString()
                )
            )
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            paymentViewModel.viewState.collect { viewState ->
                updateUI(viewState)
                binding.bntCompletePayment.setOnClickListener {
                    binding.lottieLoading.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.lottieLoading.visibility = View.GONE
                    }, 1300)
                    paymentViewModel.onFinishSelected(
                        requireContext(), Payment(
                            cardNumber = binding.inputCardNumberText.text.toString(),
                            cvv = binding.inputCardCvvText.text.toString(),
                            caducity = binding.inputCardCaducityText.text.toString()
                        ), shopCard
                    )
                }
                paymentViewModel.navigateToHome.observe(requireActivity()) {
                    it.getContentIfNotHandled()?.let {
                        view?.findNavController()?.navigate(R.id.action_paymentFragment_to_homeFragment)
                        toast("Ya tienes disponible tus tokens!")
                    }
                }
            }
        }
    }

    private fun updateUI(viewState: PaymentViewState) {
        binding.inputCardNumber.error =
            if (viewState.isValidCardNumber) null else "El numero de la tarjeta no es válido"

        binding.inputCardCvv.error =
            if (viewState.isValidCardCvv) null else "No tiene un formato correcto"

        binding.inputCardCaducity.error =
            if (viewState.isValidCardCaducity) null else "No tiene un formato correcto"

    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        with(binding) {
            if (!hasFocus) {
                paymentViewModel.onFieldsChanged(
                    Payment(
                        cardNumber = binding.inputCardNumberText.text.toString(),
                        cvv = binding.inputCardCvvText.text.toString(),
                        caducity = binding.inputCardCaducityText.text.toString()
                    )
                )
            }
        }
    }


}