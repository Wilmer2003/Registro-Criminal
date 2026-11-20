package com.example.registrocriminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.registrocriminal.databinding.FragmentListaCrimenBinding
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import java.util.Date
import java.util.UUID

class ListaCrimenesFragment : Fragment() {

    private val listaCrimenesViewModel: ListaCrimenesViewModel by viewModels()

    private var _binding: FragmentListaCrimenBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "No se puede acceder al binding porque es nulo. ¿La vista está visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaCrimenBinding.inflate(inflater, container, false)
        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listaCrimenesViewModel.crimenes.collect { crimenes ->
                    // SI LA BASE DE DATOS ESTÁ VACÍA, INSERTAMOS VARIOS CRÍMENES DIFERENTES
                    if (crimenes.isEmpty()) {
                        val crimenesIniciales = listOf(
                            Crimen(id = UUID.randomUUID(), titulo = "Robo a tienda comercial", fecha = Date(), resuelto = false),
                            Crimen(id = UUID.randomUUID(), titulo = "Hurto de vehículo deportivo", fecha = Date(), resuelto = true),
                            Crimen(id = UUID.randomUUID(), titulo = "Impresora de oficina dañada", fecha = Date(), resuelto = false),
                            Crimen(id = UUID.randomUUID(), titulo = "Intento de asalto en vía pública", fecha = Date(), resuelto = false)
                        )

                        crimenesIniciales.forEach { crimen ->
                            listaCrimenesViewModel.ingresarCrimen(crimen)
                        }
                    }

                    // ASIGNAMOS EL ADAPTADOR CON LA NAVEGACIÓN A CADA UNO
                    binding.crimenRecyclerView.adapter = CrimenAdapter(crimenes) { crimenId ->
                        findNavController().navigate(
                            ListaCrimenesFragmentDirections.mostrarCrimen(crimenId)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}