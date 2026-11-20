package com.example.registrocriminal

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fechaListener = DatePickerDialog.OnDateSetListener { _: DatePicker, anio: Int, mes: Int, dia: Int ->
            val calendarioResultado = Calendar.getInstance()
            calendarioResultado.time = args.fechaCrimen
            calendarioResultado.set(Calendar.YEAR, anio)
            calendarioResultado.set(Calendar.MONTH, mes)
            calendarioResultado.set(Calendar.DAY_OF_MONTH, dia)

            val fechaSeleccionada = calendarioResultado.time
            setFragmentResult(
                CLAVE_FECHA_SOLICITADA,
                bundleOf(CLAVE_FECHA_BUNDLE to fechaSeleccionada)
            )
        }

        val calendario = Calendar.getInstance()
        calendario.time = args.fechaCrimen

        val anioInicial = calendario.get(Calendar.YEAR)
        val mesInicial = calendario.get(Calendar.MONTH)
        val diaInicial = calendario.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            fechaListener,
            anioInicial,
            mesInicial,
            diaInicial
        )
    }

    companion object {
        const val CLAVE_FECHA_SOLICITADA = "fecha solicitada"
        const val CLAVE_FECHA_BUNDLE = "fecha bundle"
    }
}