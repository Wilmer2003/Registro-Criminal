package com.example.registrocriminal

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.registrocriminal.databinding.FragmentCrimenBinding
import kotlinx.coroutines.launch
import java.util.Date

class CrimenFragment : Fragment() {

    private var _binding: FragmentCrimenBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "No se puede acceder al binding porque es nulo. ¿La vista está visible?"
        }

    private val args: CrimenFragmentArgs by navArgs()

    private val crimenViewModel: CrimenViewModel by viewModels {
        CrimenViewModelFactory(args.crimenId)
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
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(titulo = texto.toString())
                }
            }

            chkCrimenResuelto.setOnCheckedChangeListener { _, seleccionado ->
                crimenViewModel.actualizarCrimen { anterior ->
                    anterior.copy(resuelto = seleccionado)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimenViewModel.crimen.collect { crimen ->
                    crimen?.let { actualizarUI(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.CLAVE_FECHA_SOLICITADA
        ) { _, bundle ->
            val nuevaFecha = bundle.getSerializable(DatePickerFragment.CLAVE_FECHA_BUNDLE) as Date
            crimenViewModel.actualizarCrimen { it.copy(fecha = nuevaFecha) }
        }

        setFragmentResultListener(
            FragmentoTimePicker.CLAVE_HORA_SOLICITADA
        ) { _, bundle ->
            val nuevaFechaConHora = bundle.getSerializable(FragmentoTimePicker.CLAVE_HORA_BUNDLE) as Date
            crimenViewModel.actualizarCrimen { it.copy(fecha = nuevaFechaConHora) }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val tituloActual = binding.txtTituloCrimen.text.toString().trim()

                    if (tituloActual.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Ingrese un título para el crimen antes de salir",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun actualizarUI(crimen: Crimen) {
        binding.apply {
            if (txtTituloCrimen.text.toString() != crimen.titulo) {
                txtTituloCrimen.setText(crimen.titulo)
            }
            btnFechaCrimen.text = DateFormat.format("dd/MM/yyyy", crimen.fecha).toString()
            btnFechaCrimen.setOnClickListener {
                findNavController().navigate(
                    CrimenFragmentDirections.selectorFecha(crimen.fecha)
                )
            }

            btnHoraCrimen.text = DateFormat.format("HH:mm", crimen.fecha).toString()
            btnHoraCrimen.setOnClickListener {
                findNavController().navigate(
                    CrimenFragmentDirections.selectorHora(crimen.fecha)
                )
            }

            chkCrimenResuelto.isChecked = crimen.resuelto
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}