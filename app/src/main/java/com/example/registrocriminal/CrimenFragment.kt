package com.example.registrocriminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.registrocriminal.databinding.FragmentCrimenBinding
import kotlinx.coroutines.launch
import java.util.UUID

class CrimenFragment : Fragment() {

    private var _binding: FragmentCrimenBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "No se puede acceder al binding porque es nulo."
        }

    // ID del crimen para la consulta (se puede pasar por argumentos o un ID por defecto para pruebas)
    private val crimenId: UUID by lazy {
        arguments?.getSerializable("crimen_id") as? UUID ?: UUID.randomUUID()
    }

    private val crimenDetalleViewModel: CrimenDetalleViewModel by viewModels {
        CrimenDetalleViewModelFactory(crimenId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
                crimenDetalleViewModel.actualizarCrimen { crimenViejo ->
                    crimenViejo.copy(titulo = texto.toString())
                }
            }

            chkCrimenResuelto.setOnCheckedChangeListener { _, estaChequeado ->
                crimenDetalleViewModel.actualizarCrimen { crimenViejo ->
                    crimenViejo.copy(resuelto = estaChequeado)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimenDetalleViewModel.crimen.collect { crimen ->
                    crimen?.let { actualizarUI(it) }
                }
            }
        }
    }

    private fun actualizarUI(crimen: Crimen) {
        binding.apply {
            if (txtTituloCrimen.text.toString() != crimen.titulo) {
                txtTituloCrimen.setText(crimen.titulo)
            }
            btnFechaCrimen.text = crimen.fecha.toString()
            btnFechaCrimen.isEnabled = false
            chkCrimenResuelto.isChecked = crimen.resuelto
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}