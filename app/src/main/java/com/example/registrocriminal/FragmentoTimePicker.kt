package com.example.registrocriminal

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class FragmentoTimePicker : DialogFragment() {

    private val args: FragmentoTimePickerArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val horaListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hora: Int, minuto: Int ->
            val calendarioResultado = Calendar.getInstance()
            calendarioResultado.time = args.fechaCrimen
            calendarioResultado.set(Calendar.HOUR_OF_DAY, hora)
            calendarioResultado.set(Calendar.MINUTE, minuto)
            calendarioResultado.set(Calendar.SECOND, 0)

            val fechaConNuevaHora = calendarioResultado.time
            setFragmentResult(
                CLAVE_HORA_SOLICITADA,
                bundleOf(CLAVE_HORA_BUNDLE to fechaConNuevaHora)
            )
        }

        val calendario = Calendar.getInstance()
        calendario.time = args.fechaCrimen

        val horaInicial = calendario.get(Calendar.HOUR_OF_DAY)
        val minutoInicial = calendario.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            horaListener,
            horaInicial,
            minutoInicial,
            DateFormat.is24HourFormat(requireContext())
        )
    }

    companion object {
        const val CLAVE_HORA_SOLICITADA = "hora solicitada"
        const val CLAVE_HORA_BUNDLE = "hora bundle"
    }
}