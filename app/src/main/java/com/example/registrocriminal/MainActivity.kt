package com.example.registrocriminal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "RegistroCriminal"

        //  CASO 1: MODO TABLET / HORIZONTAL
        if (findViewById<View>(R.id.contenedor_lista) != null) {
            if (supportFragmentManager.findFragmentById(R.id.contenedor_lista) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.contenedor_lista, ListaCrimenesFragment())
                    .commit()
            }
            if (supportFragmentManager.findFragmentById(R.id.contenedor_detalle) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.contenedor_detalle, CrimenFragment())
                    .commit()
            }
        }
        //  CASO 2: MODO CELULAR VERTICAL
        else {
            if (supportFragmentManager.findFragmentById(R.id.contenedor_fragmento) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.contenedor_fragmento, CrimenFragment())
                    .commit()
            }
        }
    }
}

