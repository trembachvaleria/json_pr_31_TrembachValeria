package com.example.json_pr_31_trembach

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editTags: EditText
    private lateinit var textResult: TextView
    private lateinit var btnManual: Button
    private lateinit var btnFromInput: Button
    private lateinit var btnSelfCheck: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editName = findViewById(R.id.editName)
        editPrice = findViewById(R.id.editPrice)
        editTags = findViewById(R.id.editTags)
        textResult = findViewById(R.id.textResult)
        btnManual = findViewById(R.id.btnManual)
        btnFromInput = findViewById(R.id.btnFromInput)
        btnSelfCheck = findViewById(R.id.btnSelfCheck)

        val gson = Gson()

        btnManual.setOnClickListener {
            val product = Product(
                name = "Программирование на Kotlin",
                price = 1500.0,
                tags = listOf("учебник", "программирование", "android")
            )

            val json = gson.toJson(product)
            val restored: Product = gson.fromJson(json, Product::class.java)

            textResult.text = """
                JSON:
                $json
                
                После десериализации:
                Название: ${restored.name}
                Цена: ${restored.price}
                Теги: ${restored.tags.joinToString(", ")}
            """.trimIndent()

            Log.d("MyApp", "JSON: $json")
        }

        btnFromInput.setOnClickListener {
            val name = editName.text.toString()
            val price = editPrice.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
            val tags = editTags.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val product = Product(name, price, tags)

            val json = gson.toJson(product)
            val restored: Product = gson.fromJson(json, Product::class.java)

            textResult.text = """
                JSON:
                $json
                
                После десериализации:
                Название: ${restored.name}
                Цена: ${restored.price}
                Теги: ${restored.tags.joinToString(", ")}
            """.trimIndent()

            Log.d("MyApp", "JSON: $json")
        }

        btnSelfCheck.setOnClickListener {
            val result = StringBuilder()

            val json1 = """{"name":"Книга","tags":["a","b"]}"""
            val p1: Product = gson.fromJson(json1, Product::class.java)
            result.append("1) Нет price:\n")
            result.append("OK: name=${p1.name}, price=${p1.price}, tags=${p1.tags}\n\n")

            val json2 = """{"name":"Книга","price":100.0,"tags":["a"],"extra":"x"}"""
            val p2: Product = gson.fromJson(json2, Product::class.java)
            result.append("2) Лишнее поле:\n")
            result.append("OK: name=${p2.name}, price=${p2.price}, tags=${p2.tags}\n\n")

            val json3 = """{"name":"Книга","price":"abc","tags":[]}"""
            result.append("3) price = \"abc\":\n")
            try {
                val p3: Product = gson.fromJson(json3, Product::class.java)
                result.append("OK: price=${p3.price}")
            } catch (e: Exception) {
                result.append("ОШИБКА: ${e.cause?.javaClass?.simpleName ?: e.javaClass.simpleName}: ${e.cause?.message ?: e.message}")
            }

            textResult.text = result.toString()
        }
    }
}