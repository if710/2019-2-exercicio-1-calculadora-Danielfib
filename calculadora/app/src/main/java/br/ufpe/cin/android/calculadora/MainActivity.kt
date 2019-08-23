package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var valueToStore = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_0.setOnClickListener { AddToCalcExpression(btn_0.text.toString()) }
        btn_1.setOnClickListener { AddToCalcExpression(btn_1.text.toString()) }
        btn_2.setOnClickListener { AddToCalcExpression(btn_2.text.toString()) }
        btn_3.setOnClickListener { AddToCalcExpression(btn_3.text.toString()) }
        btn_4.setOnClickListener { AddToCalcExpression(btn_4.text.toString()) }
        btn_5.setOnClickListener { AddToCalcExpression(btn_5.text.toString()) }
        btn_6.setOnClickListener { AddToCalcExpression(btn_6.text.toString()) }
        btn_7.setOnClickListener { AddToCalcExpression(btn_7.text.toString()) }
        btn_8.setOnClickListener { AddToCalcExpression(btn_8.text.toString()) }
        btn_9.setOnClickListener { AddToCalcExpression(btn_9.text.toString()) }

        btn_Subtract.setOnClickListener { AddToCalcExpression(btn_Subtract.text.toString()) }
        btn_Multiply.setOnClickListener { AddToCalcExpression(btn_Multiply.text.toString()) }
        btn_Divide.setOnClickListener { AddToCalcExpression(btn_Divide.text.toString()) }
        btn_Dot.setOnClickListener { AddToCalcExpression(btn_Dot.text.toString()) }
        btn_Equal.setOnClickListener {
            try{
                text_calc.setText(eval(text_info.text.toString()).toString())
            } catch (e: Exception){
                Toast.makeText(this, "Ih rapaz, deu ruim tua conta.", Toast.LENGTH_LONG).show()
                cleanExpression()
            }
        }
        btn_Add.setOnClickListener { AddToCalcExpression(btn_Add.text.toString()) }
        btn_LParen.setOnClickListener { AddToCalcExpression(btn_LParen.text.toString()) }
        btn_RParen.setOnClickListener { AddToCalcExpression(btn_RParen.text.toString()) }
        btn_Power.setOnClickListener { AddToCalcExpression(btn_Power.text.toString()) }
        btn_Clear.setOnClickListener {
            cleanExpression()
            cleanInfo()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("ExpressionText", text_info.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        text_info.text = savedInstanceState.getString("ExpressionText")
    }

//    val btn_1 = findViewById<Button>(R.id.btn_1)
//    val btn_2 = findViewById<Button>(R.id.btn_2)
//    val btn_3 = findViewById<Button>(R.id.btn_3)
//    val btn_Subtract = findViewById<Button>(R.id.btn_Subtract)
//
//    val btn_4 = findViewById<Button>(R.id.btn_4)
//    val btn_5 = findViewById<Button>(R.id.btn_5)
//    val btn_6 = findViewById<Button>(R.id.btn_6)
//    val btn_Multiply = findViewById<Button>(R.id.btn_Multiply)
//
//    val btn_7 = findViewById<Button>(R.id.btn_7)
//    val btn_8 = findViewById<Button>(R.id.btn_8)
//    val btn_9 = findViewById<Button>(R.id.btn_9)
//    val btn_Divide = findViewById<Button>(R.id.btn_Divide)
//
//    val btn_Dot = findViewById<Button>(R.id.btn_Dot)
//    val btn_0 = findViewById<Button>(R.id.btn_0)
//    val btn_Equal = findViewById<Button>(R.id.btn_Equal)
//    val btn_Add = findViewById<Button>(R.id.btn_Add)
//
//    val btn_LParen = findViewById<Button>(R.id.btn_LParen)
//    val btn_RParen = findViewById<Button>(R.id.btn_RParen)
//    val btn_Power = findViewById<Button>(R.id.btn_Power)
//    val btn_Clear = findViewById<Button>(R.id.btn_Clear)


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }

    fun AddToCalcExpression(toAdd: String){
        text_info.text = text_info.text.toString() + toAdd;
    }

    fun cleanExpression(){text_info.text = ""}
    fun cleanInfo(){text_calc.setText("")}
}
