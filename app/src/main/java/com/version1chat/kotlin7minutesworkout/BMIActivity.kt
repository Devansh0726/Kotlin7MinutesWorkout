package com.version1chat.kotlin7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.version1chat.kotlin7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    private var binding: ActivityBmiBinding? = null

    companion object{
        private var METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private var US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarBmiActivity)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolBarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        makeVisibleMetricUnitViews()

        binding?.rgUnits?.setOnCheckedChangeListener{_, checkId: Int ->
            if (checkId == R.id.rbMetricUnits){
                makeVisibleMetricUnitViews()
            } else{
                makeVisibleUsUnitViews()
            }

        }

        binding?.btnCalculate?.setOnClickListener {
            calculateUnits()
        }

    }

    private fun makeVisibleMetricUnitViews(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsUnitWeight?.visibility = View.GONE
        binding?.llUsUnitHeight?.visibility = View.GONE

        binding?.etUsUnitWeight?.text!!.clear()
        binding?.etUsUnitHeightFeet?.text!!.clear()
        binding?.etUsUnitHeightInch?.text!!.clear()

        binding?.llBmiResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitViews(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.GONE
        binding?.tilMetricUnitWeight?.visibility = View.GONE
        binding?.tilUsUnitWeight?.visibility = View.VISIBLE
        binding?.llUsUnitHeight?.visibility = View.VISIBLE

        binding?.etHeight?.text!!.clear()
        binding?.etWeight?.text!!.clear()

        binding?.llBmiResult?.visibility = View.INVISIBLE
    }

    private fun displayBmiResult(bmi: Float){
        var bmiLabel: String
        var bmiDescription: String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight!"
            bmiDescription = "Oops! You really need to take care of yourself! Eat more"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight!"
            bmiDescription = "Oops! You really need to take care of yourself! Eat more"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight!"
            bmiDescription = "Oops! You really need to take care of yourself! Eat more"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight!"
            bmiDescription = "Oops! You really need to take care of yourselt! Act now"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of yourselt! Act now"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese class | (Severely obese)"
            bmiDescription = "Oops! You really need to take care of yourselt! Act now"
        } else {
            bmiLabel = "Obese class | (Very severely obese)"
            bmiDescription = "Oops! You really need to take care of yourselt! Act now"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBmiLabel?.text = bmiLabel
        binding?.tvBmiDescription?.text = bmiDescription
        binding?.tvBmiValue?.text = bmiValue
        binding?.llBmiResult?.visibility = View.VISIBLE


    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if (binding?.etHeight?.text.toString().isEmpty()){
            isValid = false
        }
        if (binding?.etWeight?.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits()){

                val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etWeight?.text.toString().toFloat()
                val bmi = weightValue/(heightValue*heightValue)
                displayBmiResult(bmi)

            } else{
                Toast.makeText(this@BMIActivity,
                    "Please enter the required details", Toast.LENGTH_SHORT)
                    .show()
            }
        } else{
            if (validateUsUnits()){

                var usUnitHeightInch: String = binding?.etUsUnitHeightInch?.text.toString()
                var usUnitHeightFeet: String = binding?.etUsUnitHeightFeet?.text.toString()
                val usUnitWeightValue = binding?.etUsUnitWeight?.text.toString().toFloat()

                val usUnitHeightValue = usUnitHeightInch.toFloat() + usUnitHeightFeet.toFloat() * 12
                val bmi = 703 * usUnitWeightValue / (usUnitHeightValue * usUnitHeightValue)

                displayBmiResult(bmi)
            } else{
                Toast.makeText(this@BMIActivity,
                "Please enter the required details", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun validateUsUnits(): Boolean{
        var isValid = true

        if (binding?.etUsUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }
        if (binding?.etUsUnitHeightInch?.text.toString().isEmpty()){
            isValid = false
        }
        if (binding?.etUsUnitHeightFeet?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
}